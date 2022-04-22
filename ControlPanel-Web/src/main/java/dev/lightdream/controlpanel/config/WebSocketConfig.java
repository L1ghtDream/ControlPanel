package dev.lightdream.controlpanel.config;

import dev.lightdream.controlpanel.Main;
import dev.lightdream.logger.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        List<String> paths = new ArrayList<>();

        Main.instance.getServers().forEach(server -> {
            Logger.info("[Broker] Registering server: " + server.serverID + " @ \"/server/" + server.serverID + "/api/console\"");

            paths.add("/server/" + server.serverID + "/api/console");
        });

        config.enableSimpleBroker(paths.toArray(new String[0]));
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(@NotNull StompEndpointRegistry registry) {
        Main.instance.getServers().forEach(server -> {
            Logger.info("[Stomp] Registering server: " + server.serverID + " @ \"/server/" + server.serverID + "/api/server\"");

            registry.addEndpoint("/server/" + server.serverID + "/api/server");
            registry.addEndpoint("/server/" + server.serverID + "/api/server").withSockJS();
        });
    }

    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new SubscriptionInterceptor());
    }
}