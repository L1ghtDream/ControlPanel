package dev.lightdream.node.manager;

import com.google.common.hash.Hashing;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.utils.Utils;
import dev.lightdream.logger.Logger;
import dev.lightdream.node.Main;
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
        try (SshServer sshd = SshServer.setUpDefaultServer()) {
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

            //noinspection CodeBlock2Expr
            Utils.getNode(Main.instance.config.nodeID).getServers().forEach(server -> {
                        server.getPermissions().stream().filter(permission ->
                                        permission.permission == PermissionEnum.SERVER_FILE_MANAGER)
                                .collect(Collectors.toList()).forEach(permission -> {
                                            String username = permission.user.username + "_" + server.serverID;
                                            sftpAccounts.put(username, permission.user.password);
                                            virtualFileSystemFactory.setUserHomeDir(username, Paths.get(server.path));
                                        }
                                );
                    }

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

                return password.equals(passwordHash);
            });

            // Start and log
            sshd.start();
            Logger.good("SFTP Server started");
        }
    }

}
