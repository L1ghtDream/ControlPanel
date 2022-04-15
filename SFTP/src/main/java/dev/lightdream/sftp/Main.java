package dev.lightdream.sftp;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.Node;
import dev.lightdream.common.database.Server;
import dev.lightdream.common.dto.permission.PermissionType;
import dev.lightdream.common.manager.DatabaseManager;
import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.databasemanager.dto.DriverConfig;
import dev.lightdream.databasemanager.dto.SQLConfig;
import dev.lightdream.filemanager.FileManager;
import dev.lightdream.filemanager.FileManagerMain;
import dev.lightdream.logger.LoggableMain;
import dev.lightdream.sftp.config.Config;
import dev.lightdream.sftp.manager.SFTPServerManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends CommonMain implements DatabaseMain, FileManagerMain, LoggableMain {

    public static Main instance;

    // Dev
    private final List<Node> nodes = new ArrayList<>();
    private final List<Server> servers = new ArrayList<>();

    // Managers
    public DatabaseManager databaseManager;
    public FileManager fileManage;
    public SFTPServerManager sftpServerManager;

    // Configs
    public SQLConfig sqlConfig;
    public DriverConfig driverConfig;
    public Config config;

    public void enable() {
        instance = this;

        fileManage = new FileManager(this);
        loadConfigs();

        registerNodes();
        registerServers();

        sftpServerManager = new SFTPServerManager();
    }

    public void loadConfigs() {
        driverConfig = fileManage.load(DriverConfig.class);
        sqlConfig = fileManage.load(SQLConfig.class);
        config = fileManage.load(Config.class);
    }

    public void registerNodes() {
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
        nodes.get(0).id = 1;
    }

    public void registerServers() {
        servers.add(
                new Server(
                        "test",
                        "Test Server",
                        "/home/test",
                        nodes.get(0)
                )
        );
        servers.get(0).id = 1;
        servers.get(0).addPermission(databaseManager.getUser(1), PermissionType.SERVER_VIEW);
    }

    @Override
    public File getDataFolder() {
        return new File(System.getProperty("user.dir") + "/config");
    }


    @Override
    public SQLConfig getSqlConfig() {
        return sqlConfig;
    }

    @Override
    public DriverConfig getDriverConfig() {
        return driverConfig;
    }

    @Override
    public List<Server> getServers() {
        return servers;
    }

    @Override
    public List<Node> getNodes() {
        return nodes;
    }

    @Override
    public String qrPath() {
        return "C:/Users/raduv/OneDrive/Desktop/UserQRs/";
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public boolean debug() {
        return config.debug;
    }

    @Override
    public void log(String s) {
        System.out.println(s);
    }
}
