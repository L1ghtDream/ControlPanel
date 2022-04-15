package dev.lightdream.sftp.manager;

import com.google.common.hash.Hashing;
import dev.lightdream.common.dto.permission.PermissionType;
import dev.lightdream.common.utils.Utils;
import dev.lightdream.logger.Debugger;
import dev.lightdream.logger.Logger;
import dev.lightdream.sftp.Main;
import lombok.SneakyThrows;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class SFTPServerManager {

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    @SneakyThrows
    public SFTPServerManager() {
        SshServer sshd = SshServer.setUpDefaultServer();

        // Connection settings
        sshd.setPort(Main.instance.config.port);

        // SSH key
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(new File(Main.instance.getDataFolder() + "/host.ser")));

        // Factory
        SftpSubsystemFactory subsystemFactory = new SftpSubsystemFactory.Builder()
                .build();

        sshd.setSubsystemFactories(Arrays.asList(
                subsystemFactory
        ));

        // User home directory
        VirtualFileSystemFactory virtualFileSystemFactory = new VirtualFileSystemFactory();

        HashMap<String, String> sftpAccounts = new HashMap<>();

        Utils.getNode(Main.instance.config.nodeID).getServers().forEach(server ->
                server.permissions.stream().filter(permission ->
                        permission.permission == PermissionType.SERVER_FILE_MANAGER
                ).collect(Collectors.toList()).forEach(permission -> {
                            String username = permission.user.username + "_" + server.serverID;
                            sftpAccounts.put(username, permission.user.password);
                            Debugger.log(username + " -> " + server.path);
                            virtualFileSystemFactory.setUserHomeDir(username, Paths.get(server.path));
                        }
                )
        );

        sshd.setFileSystemFactory(virtualFileSystemFactory);

        // Sub-System
        sshd.setSubsystemFactories(Arrays.asList(
                new SftpSubsystemFactory()
        ));

        sshd.setPasswordAuthenticator((username, _password, session) -> {
            if (!sftpAccounts.containsKey(username)) {
                return false;
            }

            String passwordHash = Hashing.sha256()
                    .hashString(_password, StandardCharsets.UTF_8)
                    .toString();
            String password = sftpAccounts.get(username);

            Debugger.log(username + " : " + password + " vs " + passwordHash);

            return password.equals(passwordHash);
        });

        // Start and log
        sshd.start();
        Logger.good("SFTP Server started");
    }

}
