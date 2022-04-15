package dev.lightdream.common.sftp;

import dev.lightdream.common.sftp.database.Node;
import dev.lightdream.common.sftp.database.Server;
import dev.lightdream.common.sftp.manager.DatabaseManager;
import dev.lightdream.databasemanager.DatabaseMain;

import java.util.List;

public abstract class CommonMain implements DatabaseMain {

    public static CommonMain instance;

    public abstract List<Server> getServers();

    public abstract List<Node> getNodes();

    public abstract String qrPath();

    public abstract DatabaseManager getDatabaseManager();

}
