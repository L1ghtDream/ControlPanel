package dev.lightdream.controlpanel.dto.response;

public class Response {

    public String code;

    public Response(String code) {
        this.code = code;
    }

    public static Response OK() {
        return new Response("200");
    }


}
