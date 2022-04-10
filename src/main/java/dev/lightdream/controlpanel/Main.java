package dev.lightdream.controlpanel;

import dev.lightdream.controlpanel.controllers.EndPoints;
import dev.lightdream.controlpanel.controllers.RestEndPoints;
import dev.lightdream.controlpanel.dto.Config;
import dev.lightdream.controlpanel.managers.LogManager;
import dev.lightdream.logger.LoggableMain;

public class Main implements LoggableMain {

    public static Main instance;

    public EndPoints endPoints;
    public RestEndPoints restEndPoints;

    public Config config;

    public LogManager logManager;

    public void enable() {
        instance = this;

        this.endPoints = new EndPoints();
        this.restEndPoints = new RestEndPoints();

        logManager = new LogManager();
        logManager.registerLogListener(Executor.servers.get(0));

    }

    @Override
    public boolean debug() {
        return false;
    }

    @Override
    public void log(String s) {
        System.out.println(s);
    }

    /*Notes

    tail needs restarting after server start / stop / restart

     */
}
