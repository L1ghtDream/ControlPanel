package dev.lightdream.controlpanel.dto;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@NoArgsConstructor
public class Server {

    //Settings
    public String name;
    public String id;

    //Location
    public String path;
    public Node node;

    //Data
    public Log log;

    public Server(String id, String name, String path, Node node) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.node = node;
        this.log = new Log();
    }

    @SneakyThrows
    public void sendCommand(String command) {
        node.sendCommandToServer(command, this);
    }
}
