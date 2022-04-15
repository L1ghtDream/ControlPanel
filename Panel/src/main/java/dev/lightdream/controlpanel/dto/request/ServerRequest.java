package dev.lightdream.controlpanel.dto.request;

import dev.lightdream.common.sftp.database.Server;
import dev.lightdream.common.sftp.utils.Utils;
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