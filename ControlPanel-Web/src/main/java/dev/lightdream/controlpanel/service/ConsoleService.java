package dev.lightdream.controlpanel.service;

import dev.lightdream.common.database.Server;
import dev.lightdream.common.dto.Log;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Data
@Slf4j
@Service
public class ConsoleService {

    public static ConsoleService instance;
    private final SimpMessagingTemplate messageManager;

    ConsoleService(SimpMessagingTemplate messageManager) {
        instance = this;
        this.messageManager = messageManager;
    }

    public void sendConsole(Server server, Log log) {
        StringBuilder output = new StringBuilder();

        log.logs.forEach(logMessage -> {
            if (logMessage.equals("") || logMessage.equals(" ")) {
                return;
            }
            output.append(logMessage);
        });

        messageManager.convertAndSend("/server/" + server.serverID + "/api/console", output.toString());
    }

}