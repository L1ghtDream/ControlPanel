package dev.lightdream.controlpanel.manager;

import dev.lightdream.common.database.Server;
import dev.lightdream.common.utils.ConsoleColor;
import dev.lightdream.controlpanel.Main;
import dev.lightdream.controlpanel.dto.Log;
import dev.lightdream.controlpanel.service.ConsoleService;
import dev.lightdream.logger.Debugger;
import dev.lightdream.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LogManager {

    public HashMap<String, Log> logMap = new HashMap<>();

    public LogManager() {
        Server.getServers().forEach(this::registerLogListener);
    }

    public Log getLog(Server server) {
        Log log = logMap.get(server.getID());
        if (log == null) {
            logMap.put(server.id, new Log());
            return getLog(server);
        }
        return log;
    }

    public void registerLogListener(Server server) {
        Debugger.info("Registering log listener for server " + server.getID());
        new Thread(() -> {
            SSHManager.SSHSession session = SSHManager.createSSHSession(server.node);

            session.setCommand("tail -f " + server.path + "/session.log -n 50");

            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            session.setOutputStream(responseStream);

            while (session.isConnected()) {
                if (!responseStream.toString().equals("")) {
                    String output = responseStream.toString();

                    for (ConsoleColor consoleColor : ConsoleColor.values()) {
                        output = output.replace(consoleColor.getCode(), consoleColor.getHtml());
                    }

                    output = output.replaceAll(ConsoleColor.UNKNOWN, "");

                    List<String> logList = new ArrayList<>(List.of(output.split("\n")));
                    if (output.endsWith("\n") || output.endsWith(">")) {
                        logList.replaceAll(s -> s + "<br>");
                    }

                    if (Main.instance.config.deleteArrowStartLine) {
                        logList.replaceAll(s -> s
                                .replace("</span><span style='color:white'>><br>", "")
                                .replace("<br>>", "<br>")
                                .replaceAll("^[>]", "")
                        );
                    }

                    logList.removeIf(s ->
                            s.equals("") ||
                                    s.equals("<br>") ||
                                    s.equals("\r<br>") ||
                                    s.equals("\n")
                    );

                    Log newLog = new Log(logList);
                    getLog(server).addLog(newLog);

                    ConsoleService.instance.sendConsole(server, newLog);
                }

                responseStream = new ByteArrayOutputStream();
                session.setOutputStream(responseStream, false);

                //noinspection BusyWait
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            Logger.error("File: " + server.path + "/session.log was not found. Creating it and restarting the log listener");
            server.node.executeCommand("touch " + server.path + "/session.log");
            registerLogListener(server);
        });
    }


}
