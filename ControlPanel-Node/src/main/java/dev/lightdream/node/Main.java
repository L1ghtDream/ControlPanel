package dev.lightdream.node;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.dto.CommonConfig;
import dev.lightdream.common.manager.DatabaseManager;
import dev.lightdream.filemanager.FileManager;
import dev.lightdream.logger.Logger;
import dev.lightdream.node.controller.RestEndPoints;
import dev.lightdream.node.dto.Config;
import dev.lightdream.node.manager.RedisEventListener;
import dev.lightdream.node.manager.SFTPServerManager;
import org.springframework.boot.SpringApplication;


public class Main extends CommonMain {

    public static Main instance;

    // Managers
    public SFTPServerManager sftpServerManager;
    public RestEndPoints restEndPoints;
    public RedisEventListener redisEventListener;

    // Configs
    public Config config;

    @SuppressWarnings("resource")
    public void enable() {
        instance = this;
        Logger.init(this);

        databaseManager = new DatabaseManager(this);

        sftpServerManager = new SFTPServerManager();

        SpringApplication.run(Executor.class);

        restEndPoints = new RestEndPoints();
        redisEventListener = new RedisEventListener();

        // Infinite loop for sftp server keep alive
        //noinspection InfiniteLoopStatement,StatementWithEmptyBody
        while (true) {
        }
    }

    @Override
    public void loadConfigs(FileManager fileManager) {
        config = fileManager.load(Config.class);
        super.loadConfigs(fileManager);
    }

    @Override
    public RedisEventListener getRedisEventListener() {
        return redisEventListener;
    }

    @Override
    public CommonConfig getConfig() {
        return config;
    }
}
