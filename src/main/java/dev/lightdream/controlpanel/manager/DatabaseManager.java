package dev.lightdream.controlpanel.manager;

import dev.lightdream.controlpanel.database.Node;
import dev.lightdream.controlpanel.database.Server;
import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.databasemanager.database.ProgrammaticHikariDatabaseManager;

public class DatabaseManager extends ProgrammaticHikariDatabaseManager {
    public DatabaseManager(DatabaseMain main) {
        super(main);
    }

    @Override
    public void setup() {

        registerDataType(Node.class, "TEXT");
        registerDataType(Server.class, "TEXT");

        registerSDPair(Node.class, node -> node.id, id -> getNode((Integer) id));
        registerSDPair(Server.class, server -> server.id, id -> getServer((Integer) id));

        setup(Node.class);
        setup(Server.class);
    }

    public Node getNode(int id) {
        return null; //todo
    }

    public Server getServer(int id) {
        return null; //todo
    }
}
