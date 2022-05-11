package dev.lightdream.common.dto;

import dev.lightdream.common.CommonMain;
import dev.lightdream.common.utils.Utils;
import dev.lightdream.logger.Logger;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

@NoArgsConstructor
@AllArgsConstructor
public class BuildProperties {

    public String artifact;
    public String group;
    public String name;
    public String timestamp;
    public String version;
    public String buildType;
    public int releaseIndex;

    @SneakyThrows
    public BuildProperties load(int releaseIndex) {
        this.releaseIndex = releaseIndex;

        Properties properties = new Properties();
        InputStream inputStream = getClass().getResourceAsStream("/META-INF/build-info.properties");

        if (inputStream != null) {
            properties.load(inputStream);
        }

        artifact = properties.getProperty("build.artifact");
        group = properties.getProperty("build.group");
        name = properties.getProperty("build.name");
        timestamp = properties.getProperty("build.time");
        version = properties.getProperty("build.version");
        buildType = CommonMain.buildType;

        if (isOutdated()) {
            if (isVeryOutdated()) {
                Logger.error("" +
                        "\n#################### HIGH SEVERITY WARING ####################" +
                        "\nPlease consider upgrading to the latest version of " + getNewestVersion() + "! (version difference: " + getDifference() + ")" +
                        "\nThe version that you are using " + version + " is more then 100 version points behind the latest version " + getNewestVersion() + "." +
                        "\nThis build may not receive any support and may not have the latest security patches" +
                        "\nWe strongly advise against continuing to use this build." +
                        "\nYou can get the latest release at " + Utils.downloadURL + "." +
                        "\n#################### HIGH SEVERITY WARING ####################"
                );
            } else {
                Logger.warn("Please consider upgrading to the latest version of " + getNewestVersion() + "! (version difference: " + getDifference() + ")");
            }
        } else {
            Logger.good("You are using the latest version of " + version + "!");
        }

        return this;
    }

    public boolean isOutdated() {
        return !version.equals(getNewestVersion());
    }

    public boolean isVeryOutdated() {
        return getDifference() >= 100;
    }

    /**
     * @return Format of the version is a.b.c
     * where a is the major version, b is the minor version and c is the patch version
     * e.g. 1.0.0
     * For each major version difference the result will be +100
     * For each minor version difference the result will be +10
     * For each patch version difference the result will be +1
     */
    public int getDifference() {
        if (!isOutdated()) {
            return 0;
        }

        if(getNewestVersion().equals("UNKNOWN")) {
            return 100;
        }

        String[] newestVersion = getNewestVersion().split("\\.");
        String[] version = this.version.split("\\.");

        int difference = 0;
        for (int i = 0; i < Math.max(newestVersion.length, version.length); i++) {
            int current = (newestVersion.length < i) ? 0 : Integer.parseInt(newestVersion[i]);
            int v = (version.length < i) ? 0 : Integer.parseInt(version[i]);

            difference += (current - v) * Math.pow(10, newestVersion.length - i - 1);
        }
        return difference;
    }

    @SneakyThrows
    public String getNewestVersion() {
        try {

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(Utils.downloadURL))
                    .GET() // GET is default
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            String responseString = response.body();

            responseString = responseString.substring(
                    responseString.indexOf("<h3 class=\"d-inline\">Assets</h3>"),
                    responseString.indexOf("<span class=\"px-1 text-bold\">Source code</span>\n" +
                            "            (zip)")
            ).split("<span class=\"px-1 text-bold\">")[releaseIndex];

            responseString = responseString.substring(responseString.indexOf("/L1ghtDream/ControlPanel/releases/download/latest/"),
                    responseString.indexOf(".jar\" rel=\"nofollow\" data-skip-pjax>")
            );

            responseString = responseString.replace("/L1ghtDream/ControlPanel/releases/download/latest/", "");
            responseString = responseString.split("-")[2];

            while (responseString.split("\\.").length < 3) {
                //noinspection StringConcatenationInLoop
                responseString += ".0";
            }

            return responseString;
        } catch (Throwable t) {
            return "UNKNOWN";
        }
    }

    @SuppressWarnings("unused")
    public String getUpdateURL() {
        return Utils.downloadURL;
    }

}
