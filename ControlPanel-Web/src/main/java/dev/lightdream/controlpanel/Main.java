package dev.lightdream.controlpanel;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.GlobalPermissionContainer;
import dev.lightdream.common.database.Server;
import dev.lightdream.common.database.User;
import dev.lightdream.common.dto.cache.CacheRegistry;
import dev.lightdream.common.dto.config.CommonConfig;
import dev.lightdream.common.dto.permission.PermissionEnum;
import dev.lightdream.common.manager.DatabaseManager;
import dev.lightdream.common.utils.Utils;
import dev.lightdream.controlpanel.config.WebSocketConfig;
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

        SpringApplication.run(Executor.class);

        this.redisEventListener = new RedisEventListener(this);
        logManager = new LogManager();


        Debugger.log(User.getUser(1).generateQR(Utils.generateSecretKey()));
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
    public void registerServerLog(Server server) {
        logManager.registerLogListener(server);
    }

    @Override
    public int getReleaseIndex() {
        return 2;
    }
}

/*
TODO
Add kill screen and ctrl+c to screen when killing a server
Add notifications in the top right corner
Add hidden table row to make it so when you near-click the a permission toggle won't affect the permissions
 */