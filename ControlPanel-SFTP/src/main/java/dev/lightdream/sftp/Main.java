package dev.lightdream.sftp;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.dto.CommonConfig;
import dev.lightdream.common.manager.DatabaseManager;
import dev.lightdream.filemanager.FileManager;
import dev.lightdream.logger.Logger;
import dev.lightdream.sftp.dto.Config;
import dev.lightdream.sftp.manager.SFTPServerManager;

public class Main extends CommonMain {

    public static Main instance;

    // Managers
    public SFTPServerManager sftpServerManager;

    // Configs
    public Config config;

    public void enable() {
        instance = this;
        Logger.init(this);

        databaseManager = new DatabaseManager(this);


        sftpServerManager = new SFTPServerManager();

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
    public CommonConfig getConfig() {
        return config;
    }
}
