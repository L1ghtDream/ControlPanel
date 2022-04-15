package dev.lightdream.controlpanel.dto.request;

import dev.lightdream.controlpanel.database.Server;
import dev.lightdream.controlpanel.utils.Utils;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ServerRequest extends Request {

    public String server;

    public ServerRequest(String server) {
        this.server = server;
    }

    public Server getServer() {
        return Utils.getServer(server);
    }

}