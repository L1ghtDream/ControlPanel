package dev.lightdream.controlpanel;

import dev.lightdream.controlpanel.controllers.EndPoints;
import dev.lightdream.controlpanel.controllers.RestEndPoints;
import dev.lightdream.controlpanel.managers.LogManager;

public class Main {

    public static Main instance;

    public EndPoints endPoints;
    public RestEndPoints restEndPoints;


    public LogManager logManager;

    public void enable() {
        instance = this;

        this.endPoints = new EndPoints();
        this.restEndPoints = new RestEndPoints();

        logManager = new LogManager();
        logManager.registerLogListener(Executor.servers.get(0));

    }

    /*Notes

    tail needs restarting after server start / stop / restart

     */
}
