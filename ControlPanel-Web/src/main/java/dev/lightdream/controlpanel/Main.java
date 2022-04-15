package dev.lightdream.controlpanel;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.Node;
import dev.lightdream.common.database.Server;
import dev.lightdream.common.dto.CommonConfig;
import dev.lightdream.common.dto.permission.PermissionType;
import dev.lightdream.common.manager.DatabaseManager;
import dev.lightdream.controlpanel.controller.EndPoints;
import dev.lightdream.controlpanel.controller.RestEndPoints;
import dev.lightdream.controlpanel.dto.Config;
import dev.lightdream.controlpanel.manager.LogManager;
import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.databasemanager.dto.DriverConfig;
import dev.lightdream.databasemanager.dto.SQLConfig;
import dev.lightdream.filemanager.FileManager;
import dev.lightdream.filemanager.FileManagerMain;
import dev.lightdream.logger.LoggableMain;
import dev.lightdream.logger.Logger;
import dev.lightdream.messagebuilder.MessageBuilderManager;
import org.springframework.boot.SpringApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends CommonMain implements LoggableMain, FileManagerMain, DatabaseMain {

    // Statics
    public static Main instance;

    // Dev
    private final List<Node> nodes = new ArrayList<>();
    private final List<Server> servers = new ArrayList<>();

    // Spring
    public EndPoints endPoints;
    public RestEndPoints restEndPoints;

    //Config
    public Config config;
    public SQLConfig sqlConfig;
    public DriverConfig driverConfig;

    // Manager
    public LogManager logManager;
    public FileManager fileManager;
    public DatabaseManager databaseManager;

    public Main() {
        super();
        enable();
    }

    private void enable() {
        instance = this;
        Logger.init(this);

        fileManager = new FileManager(this);
        MessageBuilderManager.init(fileManager);
        loadConfigs();

        databaseManager = new DatabaseManager(this);

        this.endPoints = new EndPoints();
        this.restEndPoints = new RestEndPoints();

        registerNodes();
        registerServers();

        SpringApplication.run(Executor.class);

        logManager = new LogManager();
        logManager.registerLogListener(servers.get(0));

    }

    @Override
    public boolean debug() {
        return config.debug;
    }

    @Override
    public void log(String s) {
        System.out.println(s);
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
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public CommonConfig getConfig() {
        return config;
    }

    public void loadConfigs() {
        config = fileManager.load(Config.class);
        sqlConfig = fileManager.load(SQLConfig.class);
        driverConfig = fileManager.load(DriverConfig.class);
    }

    public String qrPath() {
        return "C:/Users/raduv/OneDrive/Desktop/UserQRs/";
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

    public List<Server> getServers() {
        return servers;
    }

    public List<Node> getNodes() {
        return nodes;
    }

}
