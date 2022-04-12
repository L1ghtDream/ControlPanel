package dev.lightdream.controlpanel.dto.response;

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
        return new Response("200", "OK", data);
    }

    @SuppressWarnings("unused")
    public static Response UNAUTHORISED() {
        return new Response("401", "Unauthorised");
    }

    public static Response UNAUTHORISED(String text) {
        return new Response("401", text);
    }


}
