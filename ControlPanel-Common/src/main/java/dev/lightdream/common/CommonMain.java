package dev.lightdream.common;

import dev.lightdream.common.database.Node;
import dev.lightdream.common.database.Server;
import dev.lightdream.common.dto.CommonConfig;
import dev.lightdream.common.manager.DatabaseManager;
import dev.lightdream.common.manager.SSHManager;
import dev.lightdream.databasemanager.DatabaseMain;

import java.util.List;

public abstract class CommonMain implements DatabaseMain {

    public static CommonMain instance;
    public SSHManager sshManager;

    public CommonMain() {
        instance = this;

        sshManager = new SSHManager();
    }

    public List<Server> getServers() {
        return getDatabaseManager().getServers();
    }

    public List<Node> getNodes() {
        return getDatabaseManager().getNodes();
    }

    public abstract String qrPath();

    public abstract DatabaseManager getDatabaseManager();

    public abstract CommonConfig getConfig();

    public SSHManager getSSHManager() {
        return sshManager;
    }

    public String getVersion() {
        return "1.0";
    }

}
