package dev.lightdream.controlpanel.dto;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import nl.vv32.rcon.Rcon;

@NoArgsConstructor
public class Server {

    //Settings
    public String name;
    public String id;

    //Auth
    public int port; //Server port, RCon port is +10000
    public String password;

    //Location
    public String path;
    public Node node;

    //Data
    public Log log;

    //RCon
    public Rcon rcon;

    public Server(String id, String name, int port, String password, String path, Node node) {
        this.id = id;
        this.name = name;
        this.port = port;
        this.password = password;
        this.path = path;
        this.node = node;
        this.log = new Log();
        connect();
    }


    @SneakyThrows
    public void connect() {
        rcon = Rcon.open(node.nodeIP, port + 10000);
        rcon.authenticate(password);
    }

    @SneakyThrows
    public void sendCommand(String command) {
        node.sendCommandToServer(command, this);
    }
}
