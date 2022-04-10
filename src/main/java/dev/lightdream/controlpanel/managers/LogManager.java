package dev.lightdream.controlpanel.managers;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import dev.lightdream.controlpanel.dto.Log;
import dev.lightdream.controlpanel.dto.Server;
import dev.lightdream.controlpanel.service.ConsoleService;
import dev.lightdream.controlpanel.utils.ConsoleColor;
import dev.lightdream.lambda.LambdaExecutor;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogManager {

    public LogManager() {

    }

    public void registerLogListener(Server server) {
        new Thread(() -> {
            LambdaExecutor.LambdaCatch.NoReturnLambdaCatch.executeCatch(() -> {
                if (server.node.logSession == null || !server.node.logSession.isConnected()) {
                    server.node.logSession = new JSch().getSession(server.node.username, server.node.nodeIP, 22);
                    server.node.logSession.setPassword(server.node.password);
                    server.node.logSession.setConfig("StrictHostKeyChecking", "no");
                    server.node.logSession.connect();
                }

                if (server.node.logChannel == null || !server.node.logChannel.isConnected()) {
                    server.node.logChannel = (ChannelExec) server.node.logSession.openChannel("exec");
                }

                //todo
                //server.node.logChannel.setCommand("tail -f " + server.path + "/session.log");
                server.node.logChannel.setCommand("tail -f /home/test/session.log");

                ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
                server.node.logChannel.setOutputStream(responseStream);
                server.node.logChannel.connect();

                while (server.node.logChannel.isConnected()) {
                    if (!responseStream.toString().equals("")) {
                        String output = responseStream.toString();

                        for (ConsoleColor consoleColor : ConsoleColor.values()) {
                            output = output.replace(consoleColor.getCode(), consoleColor.getHtml());
                        }

                        output = output.replaceAll(ConsoleColor.UNKNOWN, "");

                        List<String> logList = new ArrayList<>(List.of(output.split("\n")));
                        if(output.endsWith("\n")){
                            for (int i = 0; i < logList.size(); i++) {
                                logList.set(i, logList.get(i) + "<br>");
                            }
                        }

                        Log newLog = new Log(logList);
                        server.log.addLog(newLog);

                        ConsoleService.instance.sendConsole(server, newLog);
                    }

                    responseStream = new ByteArrayOutputStream();
                    server.node.logChannel.setOutputStream(responseStream);

                    //noinspection BusyWait
                    Thread.sleep(100);
                }
                registerLogListener(server);
            });
        }).start();
    }


}
