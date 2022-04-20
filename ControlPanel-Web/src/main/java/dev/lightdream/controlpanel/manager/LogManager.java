package dev.lightdream.controlpanel.manager;

import dev.lightdream.common.database.Server;
import dev.lightdream.common.manager.SSHManager;
import dev.lightdream.common.utils.ConsoleColor;
import dev.lightdream.controlpanel.dto.Log;
import dev.lightdream.controlpanel.service.ConsoleService;
import dev.lightdream.lambda.LambdaExecutor;
import dev.lightdream.logger.Debugger;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LogManager {

    public HashMap<Integer, Log> logMap = new HashMap<>();

    public LogManager() {
        //TODO Auto register log listeners for all servers
    }

    public Log getLog(Server server) {
        Log log = logMap.get(server.id);
        if (log == null) {
            logMap.put(server.id, new Log());
            return getLog(server);
        }
        return log;
    }

    public void registerLogListener(Server server) {
        new Thread(() ->
                LambdaExecutor.LambdaCatch.NoReturnLambdaCatch.executeCatch(() -> {
                    SSHManager.NodeSSH ssh = server.node.getSSH();
                    SSHManager.SSHSession session = ssh.createNew();

                    session.setCommand("tail -f " + server.path + "/session.log");

                    ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
                    session.setOutputStream(responseStream);

                    while (session.isConnected()) {
                        if (!responseStream.toString().equals("")) {
                            Debugger.log("Response stream is not empty");
                            String output = responseStream.toString();
                            Debugger.log(output);

                            for (ConsoleColor consoleColor : ConsoleColor.values()) {
                                output = output.replace(consoleColor.getCode(), consoleColor.getHtml());
                            }

                            output = output.replaceAll(ConsoleColor.UNKNOWN, "");

                            List<String> logList = new ArrayList<>(List.of(output.split("\n")));
                            if (output.endsWith("\n")) {
                                logList.replaceAll(s -> s + "<br>");
                            }

                            Log newLog = new Log(logList);
                            getLog(server).addLog(newLog);

                            ConsoleService.instance.sendConsole(server, newLog);
                        }

                        responseStream = new ByteArrayOutputStream();
                        session.setOutputStream(responseStream, false);

                        //noinspection BusyWait
                        Thread.sleep(100);
                    }
                    Debugger.log("NO! NO! NO! NO! NO! NO!");
                    registerLogListener(server);
                })).start();
    }


}
