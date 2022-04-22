package dev.lightdream.common.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Globals {

    public static Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .create();

}
