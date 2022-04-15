package dev.lightdream.controlpanel.dto.request;

import dev.lightdream.common.sftp.dto.data.Cookie;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public abstract class Request {

    public Cookie cookie;

}
