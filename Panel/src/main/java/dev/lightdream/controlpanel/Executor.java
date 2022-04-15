package dev.lightdream.controlpanel;

import dev.lightdream.logger.LoggableMainImpl;
import dev.lightdream.logger.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Executor {

    public static void main(String[] args) {
        new Main().enable();
    }

}
