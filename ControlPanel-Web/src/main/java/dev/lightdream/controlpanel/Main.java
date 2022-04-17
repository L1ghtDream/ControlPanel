package dev.lightdream.controlpanel;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.Server;
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
import dev.lightdream.logger.Debugger;
import org.springframework.boot.SpringApplication;

import java.util.List;

public class Main extends CommonMain implements FileManagerMain, DatabaseMain {

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
    public DatabaseManager databaseManager;
    List<Server> cacheServers = null;

    public Main() {
        super();
        enable();
    }

    private void enable() {
        instance = this;

        databaseManager = new DatabaseManager(this);
        createUsers(); // TODO remove for production
        createNodes(); // TODO remove for production
        createServers(); // TODO remove for production

        this.endPoints = new EndPoints();
        this.restEndPoints = new RestEndPoints();

        SpringApplication.run(Executor.class);

        logManager = new LogManager();
        logManager.registerLogListener(databaseManager.getServer("test"));

        Debugger.log(databaseManager.getServer("test").getPID());
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
                20001
        );
        databaseManager.createServer("test1", "Test Server", "/home/test", databaseManager.getNode("htz-1"), 20001);
        databaseManager.createServer("test2", "Test Server", "/home/test", databaseManager.getNode("htz-1"), 20001);
        databaseManager.createServer("test3", "Test Server", "/home/test", databaseManager.getNode("htz-1"), 20001);
        databaseManager.createServer("test4", "Test Server", "/home/test", databaseManager.getNode("htz-1"), 20001);
        databaseManager.createServer("test5", "Test Server", "/home/test", databaseManager.getNode("htz-1"), 20001);
        databaseManager.createServer("test6", "Test Server", "/home/test", databaseManager.getNode("htz-1"), 20001);
        databaseManager.createServer("test7", "Test Server", "/home/test", databaseManager.getNode("htz-1"), 20001);
        databaseManager.createServer("test8", "Test Server", "/home/test", databaseManager.getNode("htz-1"), 20001);
        databaseManager.createServer("test9", "Test Server", "/home/test", databaseManager.getNode("htz-1"), 20001);
        databaseManager.createServer("test10", "Test Server", "/home/test", databaseManager.getNode("htz-1"), 20001);
        databaseManager.createServer("test11", "Test Server", "/home/test", databaseManager.getNode("htz-1"), 20001);
        databaseManager.createServer("test12", "Test Server", "/home/test", databaseManager.getNode("htz-1"), 20001);
        databaseManager.createServer("test13", "Test Server", "/home/test", databaseManager.getNode("htz-1"), 20001);
        databaseManager.createServer("test14", "Test Server", "/home/test", databaseManager.getNode("htz-1"), 20001);
        databaseManager.createServer("test15", "Test Server", "/home/test", databaseManager.getNode("htz-1"), 20001);
        databaseManager.createServer("test16", "Test Server", "/home/test", databaseManager.getNode("htz-1"), 20001);
        databaseManager.createServer("test17", "Test Server", "/home/test", databaseManager.getNode("htz-1"), 20001);
        databaseManager.createServer("test18", "Test Server", "/home/test", databaseManager.getNode("htz-1"), 20001);
        databaseManager.createServer("test19", "Test Server", "/home/test", databaseManager.getNode("htz-1"), 20001);
        databaseManager.createServer("test20", "Test Server", "/home/test", databaseManager.getNode("htz-1"), 20001);
    }

    @Override
    public CommonConfig getConfig() {
        return config;
    }

    @Override
    public void loadConfigs(FileManager fileManager) {
        config = fileManager.load(Config.class);
        super.loadConfigs(fileManager);
    }

    public String qrPath() {
        return "C:/Users/raduv/OneDrive/Desktop/UserQRs/";
    }
}
