package dev.lightdream.controlpanel.dto;

import dev.lightdream.controlpanel.dto.request.ServerRequest;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Command extends ServerRequest {

    /**
     * The command to execute
     * Command starting with @server_manage: will be processed as server commands not console commands
     */
    public String command;

    public Command(String command) {
        this.command = command;
    }

    public boolean isServerCommand() {
        return command.startsWith("@server_manage:");
    }

    public String getCommand() {
        return command.replaceFirst("@server_manage:", "");
    }

}
