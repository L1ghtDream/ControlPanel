package dev.lightdream.controlpanel.manager;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
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
                    Session logSession = ssh.logSession;
                    ChannelExec logChannel = ssh.channel;

                    if (logSession == null || !logSession.isConnected()) {
                        logSession = new JSch().getSession(server.node.username, server.node.nodeIP, 22);
                        logSession.setPassword(server.node.password);
                        logSession.setConfig("StrictHostKeyChecking", "no");
                        logSession.connect();
                    }

                    if (logChannel == null || !logChannel.isConnected()) {
                        logChannel = (ChannelExec) logSession.openChannel("exec");
                    }

                    logChannel.setCommand("tail -f " + server.path + "/session.log");

                    ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
                    logChannel.setOutputStream(responseStream);
                    logChannel.connect();

                    while (logChannel.isConnected()) {
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
                        logChannel.setOutputStream(responseStream);

                        //noinspection BusyWait
                        Thread.sleep(100);
                    }
                    registerLogListener(server);
                })).start();
    }


}
