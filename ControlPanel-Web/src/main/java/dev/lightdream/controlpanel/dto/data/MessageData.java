package dev.lightdream.controlpanel.dto.data;

import dev.lightdream.common.utils.Utils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MessageData {

    public String server;
    public String data;

    @Override
    public String toString() {
        return Utils.toJson(this);
    }
}
