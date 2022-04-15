package dev.lightdream.controlpanel;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Executor {

    public static void main(String[] args) {
        new Main().enable();
    }

}
