package dev.lightdream.controlpanel;

import dev.lightdream.controlpanel.controller.EndPoints;
import dev.lightdream.controlpanel.controller.RestEndPoints;
import dev.lightdream.controlpanel.dto.Config;
import dev.lightdream.controlpanel.manager.LogManager;
import dev.lightdream.filemanager.FileManager;
import dev.lightdream.filemanager.FileManagerMain;
import dev.lightdream.logger.LoggableMain;
import dev.lightdream.logger.Logger;
import dev.lightdream.messagebuilder.MessageBuilderManager;

import java.io.File;

public class Main implements LoggableMain, FileManagerMain {

    public static Main instance;

    public EndPoints endPoints;
    public RestEndPoints restEndPoints;

    public Config config;

    public LogManager logManager;
    public FileManager fileManager;

    public void enable() {
        instance = this;
        Logger.init(this);

        fileManager = new FileManager(this);
        MessageBuilderManager.init(fileManager);
        loadConfigs();

        this.endPoints = new EndPoints();
        this.restEndPoints = new RestEndPoints();

        logManager = new LogManager();
        logManager.registerLogListener(Executor.servers.get(0));
    }

    @Override
    public boolean debug() {
        return config.debug;
    }

    @Override
    public void log(String s) {
        System.out.println(s);
    }

    @Override
    public File getDataFolder() {
        return new File(System.getProperty("user.dir") + "/config");
    }

    public void loadConfigs() {
        config = fileManager.load(Config.class);
    }

    /*Notes

    tail needs restarting after server start / stop / restart

     */
}
