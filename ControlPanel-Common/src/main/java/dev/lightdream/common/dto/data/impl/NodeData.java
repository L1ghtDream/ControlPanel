package dev.lightdream.common.dto.data.impl;

import dev.lightdream.common.database.Node;
import dev.lightdream.common.dto.data.Validatable;
import dev.lightdream.common.dto.data.annotation.Validate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.net.InetAddress;

@AllArgsConstructor
@NoArgsConstructor
public class NodeData extends Validatable {

    @Validate(validateMethod = "validateID")
    public String id;

    @Validate()
    public String name;
    @Validate(validateMethod = "validateIP")
    public String ip;

    @Validate(validateMethod = "validatePort")
    public int sshPort;

    @Validate(validateMethod = "validatePort")
    public int sftpPort;

    @Validate()
    public String username;

    @SuppressWarnings("unused")
    @SneakyThrows
    public boolean validateIP() {
        return InetAddress.getByName(ip).isReachable(10000);
    }

    @SuppressWarnings("unused")
    public boolean validatePort() {
        return sshPort < 65536 &&
                sshPort > 0;
    }

    @SuppressWarnings("unused")
    public boolean validateID() {
        return Node.getNode(id) != null;
    }
}
