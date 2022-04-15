package dev.lightdream.controlpanel;

import dev.lightdream.controlpanel.controller.EndPoints;
import dev.lightdream.controlpanel.controller.RestEndPoints;
import dev.lightdream.controlpanel.dto.Config;
import dev.lightdream.controlpanel.manager.SFTPServerManager;
import dev.lightdream.controlpanel.database.Node;
import dev.lightdream.controlpanel.database.Server;
import dev.lightdream.controlpanel.dto.User;
import dev.lightdream.controlpanel.dto.permission.PermissionType;
import dev.lightdream.controlpanel.manager.DatabaseManager;
import dev.lightdream.controlpanel.manager.LogManager;
import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.databasemanager.database.IDatabaseManager;
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
import java.util.Arrays;
import java.util.List;

public class Main implements LoggableMain, FileManagerMain, DatabaseMain {

    // Dev
    private static final List<Node> nodes = new ArrayList<>();
    private static final List<Server> servers = new ArrayList<>();
    private static final User user = new User(
            "admin",
            "passwd",
            "UHPVYHCTF3LRTCGAHEJCX3MYTMRHPXPM"
    );
    // Statics
    public static Main instance;
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
    public SFTPServerManager sftpServerManager;

    public void enable() {
        user.id = 1;
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

        this.sftpServerManager = new SFTPServerManager();
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
    public IDatabaseManager getDatabaseManager() {
        return databaseManager;
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

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public void registerServers() {
        servers.add(
                new Server(
                        "test",
                        "Test Server",
                        "/home/test",
                        nodes.get(0),
                        Arrays.asList(
                                20002
                        )
                )
        );
        servers.get(0).id = 1;
        servers.get(0).addPermission(user, PermissionType.SERVER_VIEW);
    }

    public List<Server> getServers() {
        return servers;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public List<User> getUsers() {
        return Arrays.asList(
                user
        );
    }
}
