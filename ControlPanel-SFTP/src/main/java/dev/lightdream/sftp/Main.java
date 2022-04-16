package dev.lightdream.sftp;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.dto.CommonConfig;
import dev.lightdream.common.manager.DatabaseManager;
import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.databasemanager.dto.DriverConfig;
import dev.lightdream.databasemanager.dto.SQLConfig;
import dev.lightdream.filemanager.FileManager;
import dev.lightdream.filemanager.FileManagerMain;
import dev.lightdream.logger.LoggableMain;
import dev.lightdream.logger.Logger;
import dev.lightdream.messagebuilder.MessageBuilderManager;
import dev.lightdream.sftp.dto.Config;
import dev.lightdream.sftp.manager.SFTPServerManager;

import java.io.File;

public class Main extends CommonMain implements DatabaseMain, FileManagerMain, LoggableMain {

    public static Main instance;

    // Managers
    public DatabaseManager databaseManager;
    public FileManager fileManager;
    public SFTPServerManager sftpServerManager;

    // Configs
    public SQLConfig sqlConfig;
    public DriverConfig driverConfig;
    public Config config;


    public void enable() {
        instance = this;
        Logger.init(this);

        fileManager = new FileManager(this);
        MessageBuilderManager.init(fileManager);
        loadConfigs();

        databaseManager = new DatabaseManager(this);


        sftpServerManager = new SFTPServerManager();

        // Infinite loop for sftp server keep alive
        //noinspection InfiniteLoopStatement,StatementWithEmptyBody
        while (true) {
        }
    }

    public void loadConfigs() {
        driverConfig = fileManager.load(DriverConfig.class);
        sqlConfig = fileManager.load(SQLConfig.class);
        config = fileManager.load(Config.class);
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
    public String qrPath() {
        return "C:/Users/raduv/OneDrive/Desktop/UserQRs/";
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public CommonConfig getConfig() {
        return config;
    }

    @Override
    public boolean debug() {
        return config.debug;
    }

    @Override
    public void log(String s) {
        System.out.println(s);
    }
}
