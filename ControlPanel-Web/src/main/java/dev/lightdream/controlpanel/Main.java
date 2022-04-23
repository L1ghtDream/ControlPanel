package dev.lightdream.controlpanel;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.User;
import dev.lightdream.common.dto.CommonConfig;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.manager.DatabaseManager;
import dev.lightdream.controlpanel.controller.EndPoints;
import dev.lightdream.controlpanel.controller.RestEndPoints;
import dev.lightdream.controlpanel.dto.Config;
import dev.lightdream.controlpanel.manager.LogManager;
import dev.lightdream.controlpanel.manager.RedisEventListener;
import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.filemanager.FileManager;
import dev.lightdream.filemanager.FileManagerMain;
import org.springframework.boot.SpringApplication;

public class Main extends CommonMain implements FileManagerMain, DatabaseMain {

    // Statics
    public static Main instance;

    // Spring
    public EndPoints endPoints;
    public RestEndPoints restEndPoints;

    //Config
    public Config config;

    // Manager
    public LogManager logManager;
    public DatabaseManager databaseManager;
    public RedisEventListener redisEventListener;

    public Main() {
        super();
        enable();
    }

    @Override
    public RedisEventListener getRedisEventListener() {
        return redisEventListener;
    }

    @Override
    public String getRedisID() {
        return "master";
    }

    @SuppressWarnings("resource")
    private void enable() {
        instance = this;

        databaseManager = new DatabaseManager(this);
        createNodes(); // TODO remove for production
        createServers(); // TODO remove for production
        createUsers(); // TODO remove for production

        SpringApplication.run(Executor.class);

        this.endPoints = new EndPoints();
        this.restEndPoints = new RestEndPoints();
        this.redisEventListener = new RedisEventListener(this);

        logManager = new LogManager();
    }

    public void createUsers() {
        databaseManager.createUser(
                "admin",
                "passwd",
                "UHPVYHCTF3LRTCGAHEJCX3MYTMRHPXPM"
        );
        User user = databaseManager.getUser("admin");
        for (PermissionEnum permission : PermissionEnum.values()) {
            databaseManager.getServer("test").addPermission(user, permission);
        }
    }

    public void createNodes() {
        databaseManager.createNode(
                "htz-1",
                "HTZ-1",
                "192.128.100.1",
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

}