package dev.lightdream.controlpanel.config;

import dev.lightdream.controlpanel.Main;
import dev.lightdream.logger.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        Main.servers.forEach(server -> {
            Logger.info("[Broker] Registering server: " + server.serverID + " ( " + "/server/" + server.serverID + "/api/console" + " )");

            config.enableSimpleBroker("/server/" + server.serverID + "/api/console");
        });

        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/server/api/server");
        registry.addEndpoint("/server/api/server").withSockJS();
    }

    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(new SubscriptionInterceptor());
    }
}