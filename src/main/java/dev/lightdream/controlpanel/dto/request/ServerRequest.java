package dev.lightdream.controlpanel.dto.request;

import dev.lightdream.controlpanel.dto.data.Cookie;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ServerRequest extends Request {

    public ServerRequest(Cookie cookie) {
        super(cookie);
    }

}