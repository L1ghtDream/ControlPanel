package dev.lightdream.controlpanel.dto.request;

import dev.lightdream.controlpanel.dto.data.Cookie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
public abstract class Request {

    public Cookie cookie;

}
