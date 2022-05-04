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

        User user = databaseManager.getUser("admin");
        GlobalPermissionContainer.getInstance().addPermission(user, PermissionEnum.GLOBAL_ADMIN);
        GlobalPermissionContainer.getInstance().addPermission(user, PermissionEnum.GLOBAL_MANAGE_NODES);
        GlobalPermissionContainer.getInstance().addPermission(user, PermissionEnum.GLOBAL_MANAGE_USERS);
        GlobalPermissionContainer.getInstance().addPermission(user, PermissionEnum.GLOBAL_VIEW);
        GlobalPermissionContainer.getInstance().addPermission(user, PermissionEnum.GLOBAL_CREATE_SERVER);
    }

    public void createNodes() {
        databaseManager.createNode("htz-1", "HTZ-1", "htz1.original.gg", "root", 22, 2222);
        databaseManager.createNode("htz-5", "HTZ-5", "htz5.original.gg", "root", 22, 2222);
    }

    public void createServers() {
        databaseManager.createServer("SlimeSkyBlock", "Test Server 2", "/home/SlimeSkyBlock/", databaseManager.getNode("htz-5"), 24000, "JDK_16", "8G", "slimeskyblock.jar", "-javaagent:sw.jar", false);
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

/*
TODO
When creating a server create its home path
registerServerWS when creating new server
Redirect the user to the new server after creating a new server
Add kill screen and ctrl+c to screen when killing a server
 */