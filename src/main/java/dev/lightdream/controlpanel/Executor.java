package dev.lightdream.controlpanel;

import dev.lightdream.controlpanel.database.Node;
import dev.lightdream.controlpanel.database.Server;
import dev.lightdream.controlpanel.dto.User;
import dev.lightdream.controlpanel.dto.permission.PermissionType;
import dev.lightdream.logger.LoggableMainImpl;
import dev.lightdream.logger.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class Executor {

    public static void main(String[] args) {
        new Main().enable();
    }

}
