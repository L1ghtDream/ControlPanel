package dev.lightdream.node;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.database.Node;
import dev.lightdream.common.database.Server;
import dev.lightdream.common.dto.config.CommonConfig;
import dev.lightdream.common.manager.DatabaseManager;
import dev.lightdream.filemanager.FileManager;
import dev.lightdream.lambda.ScheduleUtils;
import dev.lightdream.logger.Logger;
import dev.lightdream.node.dto.Config;
import dev.lightdream.node.manager.CacheManager;
import dev.lightdream.node.manager.RedisEventListener;
import dev.lightdream.node.manager.SFTPServerManager;
import dev.lightdream.node.manager.ScheduleManager;


public class Main extends CommonMain {

    public static Main instance;

    // Managers
    public SFTPServerManager sftpServerManager;
    public RedisEventListener redisEventListener;
    public CacheManager cacheManager;
    public ScheduleManager scheduleManager;

    // Configs
    public Config config;

    public void enable() {
        instance = this;
        Logger.init(this);

        databaseManager = new DatabaseManager(this);
        sftpServerManager = new SFTPServerManager(this);

        redisEventListener = new RedisEventListener(this);
        cacheManager = new CacheManager(this);
        scheduleManager = new ScheduleManager();

        new Thread(() -> {
            ScheduleUtils.runTaskTimer(() -> {
                // noop
            }, 0, 60 * 1000L); // 1 minute
        }).start();
    }

    @Override
    public void loadConfigs(FileManager fileManager) {
        config = fileManager.load(Config.class);
        super.loadConfigs(fileManager);
    }

    @Override
    public void registerServerLog(Server server) {

    }

    @Override
    public int getReleaseIndex() {
        return 1;
    }

    @Override
    public RedisEventListener getRedisEventListener() {
        return redisEventListener;
    }

    @Override
    public String getRedisID() {
        return config.nodeID;
    }

    @Override
    public CommonConfig getConfig() {
        return config;
    }

    public Node getNode() {
        return databaseManager.getNode(config.nodeID);
    }
}
