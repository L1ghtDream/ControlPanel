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
        setup(Node.class);
        setup(Server.class);
    }
}
