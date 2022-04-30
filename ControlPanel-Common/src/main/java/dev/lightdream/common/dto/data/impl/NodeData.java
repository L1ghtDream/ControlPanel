package dev.lightdream.common.dto.data.impl;

import dev.lightdream.common.dto.data.Validatable;
import dev.lightdream.common.dto.data.annotation.Validate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.net.InetAddress;

@AllArgsConstructor
@NoArgsConstructor
public class NodeData extends Validatable {

    @Validate()
    public String name;
    @Validate(validateMethod = "validateIP")
    public String ip;

    @Validate(validateMethod = "validatePort")
    public int sshPort;
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
}
