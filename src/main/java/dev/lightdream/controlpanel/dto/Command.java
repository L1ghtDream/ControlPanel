package dev.lightdream.controlpanel.dto;

import dev.lightdream.controlpanel.dto.request.ServerRequest;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Command extends ServerRequest {

    public String command;

    public Command(String command) {
        this.command = command;
    }

}
