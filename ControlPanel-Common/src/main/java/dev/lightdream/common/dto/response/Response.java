package dev.lightdream.common.dto.response;

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
        return OK(null);
    }

    public static Response OK(Object data) {
        return new Response("200", "OK", data);
    }

    public static Response UNAUTHORISED() {
        return UNAUTHORISED("Unauthorised");
    }

    public static Response UNAUTHORISED(String text) {
        return new Response("401", text);
    }

    public static Response LOCKED(String text) {
        return new Response("423", text);
    }

    public static Response BAD_DATA(Object data) {
        return new Response("400", "Bad Data", data);
    }

    @SuppressWarnings("unused")
    public static Response BAD_DATA() {
        return BAD_DATA(null);
    }

    public static Response NOT_FOUND() {
        return new Response("404", "Not Found");
    }


}
