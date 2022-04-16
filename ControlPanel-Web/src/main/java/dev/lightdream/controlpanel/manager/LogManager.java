package dev.lightdream.controlpanel.manager;

import dev.lightdream.common.database.Server;
import dev.lightdream.common.manager.SSHManager;
import dev.lightdream.common.utils.ConsoleColor;
import dev.lightdream.controlpanel.dto.Log;
import dev.lightdream.controlpanel.service.ConsoleService;
import dev.lightdream.lambda.LambdaExecutor;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LogManager {

    public HashMap<Integer, Log> logMap = new HashMap<>();

    public LogManager() {

    }

    public Log getLog(Server server) {
        Log log = logMap.get(server.id);
        if (log == null) {
            return logMap.put(server.id, new Log());
        }
        return log;
    }

    public void registerLogListener(Server server) {
        new Thread(() ->
                LambdaExecutor.LambdaCatch.NoReturnLambdaCatch.executeCatch(() -> {
                    SSHManager.NodeSSH ssh = server.node.getSSH();

                    ssh.setCommandLog("tail -f " + server.path + "/session.log");

                    ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
                    ssh.setOutputStreamLog(responseStream);

                    while (ssh.isConnectedLog()) {
                        if (!responseStream.toString().equals("")) {
                            String output = responseStream.toString();

                            for (ConsoleColor consoleColor : ConsoleColor.values()) {
                                output = output.replace(consoleColor.getCode(), consoleColor.getHtml());
                            }

                            output = output.replaceAll(ConsoleColor.UNKNOWN, "");

                            List<String> logList = new ArrayList<>(List.of(output.split("\n")));
                            if (output.endsWith("\n")) {
                                for (int i = 0; i < logList.size(); i++) {
                                    logList.set(i, logList.get(i) + "<br>");
                                }
                            }

                            Log newLog = new Log(logList);
                            getLog(server).addLog(newLog);

                            ConsoleService.instance.sendConsole(server, newLog);
                        }

                        responseStream = new ByteArrayOutputStream();
                        ssh.setOutputStreamLog(responseStream);

                        //noinspection BusyWait
                        Thread.sleep(100);
                    }
                    registerLogListener(server);
                })).start();
    }


}
