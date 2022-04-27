package dev.lightdream.controlpanel.dto.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.net.InetAddress;

@AllArgsConstructor
@NoArgsConstructor
public class NodeData extends Validatable {

    @Validate(validateMethod = "validateName")
    public String name;
    @Validate(validateMethod = "validateIP")
    public String ip;

    @Validate(validateMethod = "validateSshPort")
    public int sshPort;
    @Validate(validateMethod = "validateUsername")
    public String username;

    @SuppressWarnings("unused")
    private boolean validateName() {
        return !name.equals("") && !name.equals(" ");
    }

    @SuppressWarnings("unused")
    private boolean validateUsername() {
        return !username.equals("") && !username.equals(" ");
    }

    @SuppressWarnings("unused")
    @SneakyThrows
    private boolean validateIP() {
        return InetAddress.getByName(ip).isReachable(10000);
    }

    @SuppressWarnings("unused")
    private boolean validateSshPort() {
        return sshPort < 65536 &&
                sshPort > 0;
    }
}
