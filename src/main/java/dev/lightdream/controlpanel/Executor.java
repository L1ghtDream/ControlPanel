package dev.lightdream.controlpanel;

import dev.lightdream.controlpanel.dto.Node;
import dev.lightdream.controlpanel.dto.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class Executor {

    public static List<Node> nodes = new ArrayList<>();
    public static List<Server> servers = new ArrayList<>();

    public static void main(String[] args) {
        registerNodes();
        registerServers();

        SpringApplication.run(Executor.class, args);
        new Main().enable();
    }

    public static void registerNodes() {
        nodes.add(
                new Node(
                        "htz-1",
                        "HTZ-1",
                        "162.55.103.213",
                        "162.55.103.213",
                        "kvkfBt33vBxNCdBw",
                        "root",
                        22
                )
        );
    }

    public static void registerServers() {
        servers.add(
                new Server(
                        "test",
                        "Test Server",
                        "/home/test",
                        nodes.get(0)
                )
        );
    }

}
