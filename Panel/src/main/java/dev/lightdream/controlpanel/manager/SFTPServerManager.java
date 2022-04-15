package dev.lightdream.controlpanel.manager;

import dev.lightdream.controlpanel.Main;
import dev.lightdream.logger.Debugger;
import dev.lightdream.logger.Logger;
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
    public SFTPServerManager() {
        SshServer sshd = SshServer.setUpDefaultServer();

        // Connection settings
        sshd.setPort(2222);

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

        virtualFileSystemFactory.setUserHomeDir("admin", Paths.get("C:\\Users\\raduv\\OneDrive\\Desktop\\MinecraftServer"));
        sshd.setFileSystemFactory(virtualFileSystemFactory);

        // ???
        sshd.setSubsystemFactories(Arrays.asList(
                new SftpSubsystemFactory()
        ));

        sshd.setPasswordAuthenticator((username, password, session) -> {
            return username.equals("admin") && password.equals("passwd");
        });

        // Start and log
        sshd.start();
        Logger.good("SFTP Server started");
        Debugger.log("Sub-System Factories: " + sshd.getSubsystemFactories());
    }

}
