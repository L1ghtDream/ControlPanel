package dev.lightdream.common;

import dev.lightdream.common.database.Node;
import dev.lightdream.common.database.Server;
import dev.lightdream.common.dto.CommonConfig;
import dev.lightdream.common.dto.redis.RedisConfig;
import dev.lightdream.common.dto.redis.command.PublicKeySend;
import dev.lightdream.common.manager.*;
import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.databasemanager.dto.DriverConfig;
import dev.lightdream.databasemanager.dto.SQLConfig;
import dev.lightdream.filemanager.FileManager;
import dev.lightdream.filemanager.FileManagerMain;
import dev.lightdream.logger.LoggableMain;
import dev.lightdream.logger.Logger;
import dev.lightdream.messagebuilder.MessageBuilderManager;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract class CommonMain implements DatabaseMain, LoggableMain, FileManagerMain {

    public static CommonMain instance;

    // Config
    public DriverConfig driverConfig;
    public SQLConfig sqlConfig;
    public RedisConfig redisConfig;

    // Managers
    public SSHManager sshManager;
    public CacheManager cacheManager;
    public DatabaseManager databaseManager;
    public RedisManager redisManager;

    @SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
    private FileManager fileManager;

    public CommonMain() {
        instance = this;
        Logger.init(this);

        fileManager = new FileManager(this);
        MessageBuilderManager.init(fileManager);
        loadConfigs(fileManager);

        databaseManager = new DatabaseManager(this);
        redisManager = new RedisManager(this);

        sshManager = new SSHManager();
        cacheManager = new CacheManager();

        new RedisEventListener();

        redisManager.send(new PublicKeySend("htz-1",
                new String(new EncryptionManager().getKeyPair().getPublic().getEncoded(), StandardCharsets.UTF_8))
        );
    }

    public List<Server> getServers() {
        return getDatabaseManager().getServers();
    }

    public List<Node> getNodes() {
        return getDatabaseManager().getNodes();
    }

    public String qrPath() {
        return getConfig().qrStorageLocation;
    }

    public abstract CommonConfig getConfig();

    public SSHManager getSSHManager() {
        return sshManager;
    }

    public String getVersion() {
        return "1.0";
    }

    @Override
    public boolean debug() {
        return getConfig().debug;
    }

    @Override
    public void log(String s) {
        System.out.println(s);
    }

    public void loadConfigs(FileManager fileManager) {
        sqlConfig = fileManager.load(SQLConfig.class);
        driverConfig = fileManager.load(DriverConfig.class);
        redisConfig = fileManager.load(RedisConfig.class);
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

}
