package dev.lightdream.controlpanel.dto;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import dev.lightdream.lambda.LambdaExecutor;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;

@NoArgsConstructor
public class Node {

    //Settings
    public String id;
    public String name;

    //Credentials
    public String nodeIP;
    public String publicIP;
    public String password;
    public String username;
    public int sshPort;

    //Connections
    public Session session;
    public ChannelExec channel;

    public Session logSession;
    public ChannelExec logChannel;

    public Node(String id, String name, String nodeIP, String publicIP, String password, String username, int sshPort) {
        this.id = id;
        this.name = name;
        this.nodeIP = nodeIP;
        this.publicIP = publicIP;
        this.password = password;
        this.username = username;
        this.sshPort = sshPort;
    }

    public String sendCommand(String command) {
        return LambdaExecutor.LambdaCatch.ReturnLambdaCatch.executeCatch(() -> {
            if (this.session == null || !this.session.isConnected()) {
                this.session = new JSch().getSession(this.username, this.nodeIP, 22);
                this.session.setPassword(this.password);
                this.session.setConfig("StrictHostKeyChecking", "no");
                this.session.connect();
            }

            if (this.channel == null || !this.channel.isConnected()) {
                this.channel = (ChannelExec) this.session.openChannel("exec");
            }

            this.channel.setCommand(command);

            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            this.channel.setOutputStream(responseStream);
            this.channel.connect();

            while (this.channel.isConnected()) {
                //noinspection BusyWait
                Thread.sleep(100);
            }

            return responseStream.toString();
        });
    }

    @SuppressWarnings("UnusedReturnValue")
    public String sendCommandToServer(String command, Server server) {
        return sendCommand("screen -S " + server.id + " -X stuff '" + command + "\\n'");
    }


}
