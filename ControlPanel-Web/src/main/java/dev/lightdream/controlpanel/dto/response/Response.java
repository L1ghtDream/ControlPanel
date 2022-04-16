package dev.lightdream.controlpanel.dto.response;

import dev.lightdream.logger.Debugger;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Response {

    public String code;
    public String text;
    public Object data;

    public Response(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public static Response OK() {
        return new Response("200", "OK");
    }

    public static Response OK(Object data) {
        Response r = new Response("200", "OK", data);
        Debugger.log(data);
        return r;
    }

    @SuppressWarnings("unused")
    public static Response UNAUTHORISED() {
        return new Response("401", "Unauthorised");
    }

    public static Response UNAUTHORISED(String text) {
        return new Response("401", text);
    }

    public static Response LOCKED(String text) {
        return new Response("423", text);
    }


}
