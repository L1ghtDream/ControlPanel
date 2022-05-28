package dev.lightdream.common.dto.request;

import dev.lightdream.common.database.Server;
import dev.lightdream.common.utils.Utils;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ServerRequest extends Request {

    public String server;

    @SuppressWarnings("unused")
    public ServerRequest(String server) {
        this.server = server;
    }

    public Server getServer() {
        return Utils.getServer(server);
    }

}