package dev.lightdream.common.dto;

import dev.lightdream.common.CommonMain;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.InputStream;
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

    @SneakyThrows
    public BuildProperties load(){
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
        return this;
    }

}
