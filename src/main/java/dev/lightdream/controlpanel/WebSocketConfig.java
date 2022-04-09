package dev.lightdream.controlpanel;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        Executor.servers.forEach(server -> {
            System.out.println("[Broker] Registering server: " + server.id + " ( " + "/server/" + server.id + "/api/console" + " )");

            config.enableSimpleBroker("/server/" + server.id + "/api/console");
        });

        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/server/api/server");
        registry.addEndpoint("/server/api/server").withSockJS();
    }
}