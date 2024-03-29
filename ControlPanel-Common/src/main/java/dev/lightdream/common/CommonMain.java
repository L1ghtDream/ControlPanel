package dev.lightdream.common;

import dev.lightdream.common.database.Node;
import dev.lightdream.common.database.Server;
import dev.lightdream.common.dto.BuildProperties;
import dev.lightdream.common.dto.config.CommonConfig;
import dev.lightdream.common.dto.redis.RedisConfig;
import dev.lightdream.common.manager.DatabaseManager;
import dev.lightdream.common.manager.RedisEventListener;
import dev.lightdream.common.manager.RedisEventManager;
import dev.lightdream.common.manager.RedisManager;
import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.databasemanager.config.SQLConfig;
import dev.lightdream.filemanager.FileManager;
import dev.lightdream.filemanager.FileManagerMain;
import dev.lightdream.logger.LoggableMain;
import dev.lightdream.logger.Logger;
import dev.lightdream.messagebuilder.MessageBuilderManager;

import java.io.File;
import java.util.List;

public abstract class CommonMain implements DatabaseMain, LoggableMain, FileManagerMain {

    public static final String buildType = "Dev Build";
    public static CommonMain instance;
    // Config
    public SQLConfig sqlConfig;
    public RedisConfig redisConfig;
    public BuildProperties buildProperties;

    // Managers
    public DatabaseManager databaseManager;
    public RedisManager redisManager;
    public RedisEventManager redisEventManager = new RedisEventManager();

    @SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
    private FileManager fileManager;

    public CommonMain() {
        instance = this;
        Logger.init(this);

        fileManager = new FileManager(this);
        MessageBuilderManager.init(fileManager);
        Logger.info("Using config located at " + getDataFolder().getPath());
        loadConfigs(fileManager);

        databaseManager = new DatabaseManager(this);
        redisManager = new RedisManager(getRedisID());

        buildProperties = new BuildProperties().load(getReleaseIndex());
    }

    /**
     * @return The index of the version from github
     * 0 for Common
     * 1 for Node
     * 2 for Web
     */
    public abstract int getReleaseIndex();

    @SuppressWarnings("unused")
    public abstract RedisEventListener getRedisEventListener();

    public abstract String getRedisID();

    @Deprecated
    public List<Server> getServers() {
        return getDatabaseManager().getServers();
    }

    @Deprecated
    public List<Server> getServers(Node node) {
        return getDatabaseManager().getServers(node);
    }

    public List<Node> getNodes() {
        return getDatabaseManager().getNodes();
    }

    public abstract CommonConfig getConfig();

    public String getVersion() {
        return "1.0";
    }



    public void loadConfigs(FileManager fileManager) {
        sqlConfig = fileManager.load(SQLConfig.class);
        redisConfig = fileManager.load(RedisConfig.class);
    }

    @Override
    public File getDataFolder() {
        return new File(System.getProperty("user.dir") + "/config");
    }



    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public abstract void registerServerLog(Server server);

    @Override
    public boolean debugToConsole() {
        return getConfig().debug;
    }

    @Override
    public SQLConfig getSqlConfig() {
        return sqlConfig;
    }
}
