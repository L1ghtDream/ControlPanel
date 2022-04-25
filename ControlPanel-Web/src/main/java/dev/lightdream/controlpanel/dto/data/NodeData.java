package dev.lightdream.controlpanel.dto.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class NodeData {

    public String name;
    public String ip;
    public int sshPort;
    public String username;

}
