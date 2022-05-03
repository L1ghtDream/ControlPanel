package dev.lightdream.controlpanel;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.GlobalPermissionContainer;
import dev.lightdream.common.database.User;
import dev.lightdream.common.dto.cache.CacheRegistry;
import dev.lightdream.common.dto.config.CommonConfig;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.manager.DatabaseManager;
import dev.lightdream.common.utils.Utils;
import dev.lightdream.controlpanel.dto.Config;
import dev.lightdream.controlpanel.manager.LogManager;
import dev.lightdream.controlpanel.manager.RedisEventListener;
import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.filemanager.FileManager;
import dev.lightdream.filemanager.FileManagerMain;
import dev.lightdream.logger.Debugger;
import org.springframework.boot.SpringApplication;


public class Main extends CommonMain implements FileManagerMain, DatabaseMain {

    // Statics
    public static Main instance;

    // Spring
    //public EndPoints endPoints;
    //public RestEndPoints restEndPoints;

    //Config
    public Config config;

    // Manager
    public LogManager logManager;
    public DatabaseManager databaseManager;
    public RedisEventListener redisEventListener;
    public CacheRegistry serversCache = new CacheRegistry();

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

        this.redisEventListener = new RedisEventListener(this);

        logManager = new LogManager();

        Debugger.log(User.getUser(1).generateQR(Utils.generateSecretKey()));
    }

    public void createUsers() {
        databaseManager.createUser("admin", "passwd");
        databaseManager.createUser("test", "passwd");

        User user = databaseManager.getUser("admin");
        for (PermissionEnum permission : PermissionEnum.values()) {
            databaseManager.getServer("test1").addPermission(user, permission);
            databaseManager.getServer("test2").addPermission(user, permission);
            databaseManager.getServer("test3").addPermission(user, permission);
        }
        GlobalPermissionContainer.getInstance().addPermission(user, PermissionEnum.GLOBAL_ADMIN);
        GlobalPermissionContainer.getInstance().addPermission(user, PermissionEnum.GLOBAL_MANAGE_NODES);
        GlobalPermissionContainer.getInstance().addPermission(user, PermissionEnum.GLOBAL_MANAGE_USERS);
        GlobalPermissionContainer.getInstance().addPermission(user, PermissionEnum.GLOBAL_VIEW);
    }

    public void createNodes() {
        databaseManager.createNode("htz-1", "HTZ-1", "htz1.original.gg", "root", 22, 2222);
    }

    public void createServers() {
        databaseManager.createServer("test1", "Test Server 1", "/home/test1", databaseManager.getNode("htz-1"), 20001, "JDK_16", "8G", "skyblock.jar", "", false);
        databaseManager.createServer("test2", "Test Server 2", "/home/test2", databaseManager.getNode("htz-1"), 20002, "JDK_16", "8G", "skyblock.jar", "", false);
        databaseManager.createServer("test3", "Test Server 3", "/home/test3", databaseManager.getNode("htz-1"), 20003, "JDK_16", "8G", "skyblock.jar", "", false);
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

    @Override
    public int getReleaseIndex() {
        return 2;
    }
}