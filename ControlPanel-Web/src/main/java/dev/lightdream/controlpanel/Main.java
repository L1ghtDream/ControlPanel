package dev.lightdream.controlpanel;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.dto.CommonConfig;
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

public class Main extends CommonMain implements LoggableMain, FileManagerMain, DatabaseMain {

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
        createUsers(); // TODO remove for production
        createNodes(); // TODO remove for production
        createServers(); // TODO remove for production

        this.endPoints = new EndPoints();
        this.restEndPoints = new RestEndPoints();

        SpringApplication.run(Executor.class);

        logManager = new LogManager();
        logManager.registerLogListener(databaseManager.getServer("test"));

    }

    public void createUsers() {
        databaseManager.createUser(
                "admin",
                "passwd",
                "UHPVYHCTF3LRTCGAHEJCX3MYTMRHPXPM"
        );
    }

    public void createNodes() {
        databaseManager.createNode(
                "htz-1",
                "HTZ-1",
                "htz1.original.gg",
                config.mockServerPassword,
                "root",
                22
        );
    }

    public void createServers() {
        databaseManager.createServer(
                "test",
                "Test Server",
                "/home/test",
                databaseManager.getNode("htz-1"),
                20002
        );
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

}
