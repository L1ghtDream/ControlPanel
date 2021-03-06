package dev.lightdream.common.utils;

public enum ConsoleColor {

    // Reset
    RESET("\u001B[m", "</span><span style='color:white'>"),
    RESET_2("\u001B[0m", RESET.html),
    RESET_4("\u001B[2K", RESET.html),

    // Regular Colors
    BLACK("\u001B[30m;1m", RESET.html),
    BLACK_2("\u001B[30m", RESET.html),
    BLACK_3("\u001B[0;30m", RESET.html),
    BLACK_4("\u001B[30;1m", RESET.html),

    RED("\u001B[31m;1m", "</span><span style='color:red'>"),
    RED_2("\u001B[31m", RED.html),
    RED_3("\u001B[0;31m", RED.html),
    RED_4("\u001B[31;1m", RED.html),

    GREEN("\u001B[32m;1m", "</span><span style='color:green'>"),
    GREEN_2("\u001B[32m", GREEN.html),
    GREEN_3("\u001B[0;32m", GREEN.html),
    GREEN_4("\u001B[32;1m", GREEN.html),

    YELLOW("\u001B[33m;1m", "</span><span style='color:yellow'>"),
    YELLOW_2("\u001B[33m", YELLOW.html),
    YELLOW_3("\u001B[0;33m", YELLOW.html),
    YELLOW_4("\u001B[33;1m", YELLOW.html),

    BLUE("\u001B[34m;1m", "</span><span style='color:blue'>"),
    BLUE_2("\u001B[34m", BLUE.html),
    BLUE_3("\u001B[0;34m", BLUE.html),
    BLUE_4("\u001B[34;1m", BLUE.html),

    PURPLE("\u001B[35m;1m", "</span><span style='color:purple'>"),
    PURPLE_2("\u001B[35m", PURPLE.html),
    PURPLE_3("\u001B[0;35m", PURPLE.html),
    PURPLE_4("\u001B[35;1m", PURPLE.html),

    CYAN("\u001B[36m;1m", "</span><span style='color:cyan'>"),
    CYAN_2("\u001B[36m", CYAN.html),
    CYAN_3("\u001B[0;36m", CYAN.html),
    CYAN_4("\u001B[36;1m", CYAN.html),

    WHITE("\u001B[37m;1m", "</span><span style='color:white'>"),
    WHITE_2("\u001B[37m", WHITE.html),
    WHITE_3("\u001B[0;37m", WHITE.html),
    WHITE_4("\u001B[37;1m", WHITE.html),

    // Bold Colors
    BLACK_BOLD("\u001B[0;30;1m", "</span><span style='color:black; font-weight: bold;'>"),
    RED_BOLD("\u001B[0;31;1m", "</span><span style='color:red; font-weight: bold;'>"),
    GREEN_BOLD("\u001B[0;32;1m", "</span><span style='color:green; font-weight: bold;'>"),
    YELLOW_BOLD("\u001B[0;33;1m", "</span><span style='color:yellow; font-weight: bold;'>"),
    BLUE_BOLD("\u001B[0;34;1m", "</span><span style='color:blue; font-weight: bold;'>"),
    PURPLE_BOLD("\u001B[0;35;1m", "</span><span style='color:purple; font-weight: bold;'>"),
    CYAN_BOLD("\u001B[0;36;1m", "</span><span style='color:cyan; font-weight: bold;'>"),
    WHITE_BOLD("\u001B[0;37;1m", "</span><span style='color:white; font-weight: bold;'>");

    public final static String UNKNOWN = "[\\x00-\\x1F].[0-9,;]+[m]";
    private final String code;
    private final String html;

    ConsoleColor(String code, String html) {
        this.code = code;
        this.html = html;
    }

    public String getCode() {
        return code;
    }

    public String getHtml() {
        return html;
    }
}