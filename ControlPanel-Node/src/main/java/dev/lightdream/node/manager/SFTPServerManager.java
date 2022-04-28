package dev.lightdream.node.manager;

import dev.lightdream.common.database.Server;
import dev.lightdream.common.database.User;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.logger.Logger;
import dev.lightdream.node.Main;
import lombok.SneakyThrows;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

public class SFTPServerManager {

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    @SneakyThrows
    public SFTPServerManager(Main main) {
        // Needs to not close the port
        //noinspection resource
        SshServer sshd = SshServer.setUpDefaultServer();
        // Connection settings
        sshd.setPort(main.config.port);

        // SSH key
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(new File(main.getDataFolder() + "/host.ser")));

        // Factory
        SftpSubsystemFactory subsystemFactory = new SftpSubsystemFactory.Builder()
                .build();

        sshd.setSubsystemFactories(Arrays.asList(
                subsystemFactory
        ));

        // User home directory
        VirtualFileSystemFactory virtualFileSystemFactory = new VirtualFileSystemFactory();
        sshd.setFileSystemFactory(virtualFileSystemFactory);

        // Sub-System
        sshd.setSubsystemFactories(Arrays.asList(
                new SftpSubsystemFactory()
        ));

        sshd.setPasswordAuthenticator((username, password, session) -> {

            if (username.split("_").length != 2) {
                return false;
            }

            String user_username = username.split("_")[0];
            String serverID = username.split("_")[1];

            User user = User.getUser(user_username);
            Server server = Server.getServer(serverID);

            virtualFileSystemFactory.setUserHomeDir(username, Paths.get(server.path));

            if(!user.hasPermission(server, PermissionEnum.SERVER_FILE_MANAGER)){
                return false;
            }

            return user.checkRawPassword(password);
        });

        // Start and log
        sshd.start();
        Logger.good("SFTP Server started");
    }

}
