package dev.lightdream.controlpanel.config;

import dev.lightdream.common.database.Server;
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

    public static WebSocketConfig instance;
    private final List<String> paths = new ArrayList<>();
    public MessageBrokerRegistry config;
    public StompEndpointRegistry registry;

    @Override
    public void configureMessageBroker(@NotNull MessageBrokerRegistry config) {
        if (instance == null) {
            instance = this;
        }
        if (this.config == null) {
            this.config = config;
        }

        Logger.info("[Broker] Registering @ \"/server/api/console\"");

        paths.add("/server/api/console");// /


        config.enableSimpleBroker(paths.toArray(new String[0]));
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(@NotNull StompEndpointRegistry registry) {
        if (instance == null) {
            instance = this;
        }
        if (this.registry == null) {
            this.registry = registry;
        }

        //Server.getServers().forEach(server -> {
        //    Logger.info("[Stomp] Registering server: " + server.id + " @ \"/server/" + server.id + "/api/server\"");

        registry.addEndpoint("/server/api/server");
        registry.addEndpoint("/server/api/server").withSockJS();
        //});
    }

    public void registerServerWS(Server server) {
        //Logger.info("[Broker] Registering server: " + server.id + " @ \"/server/" + server.id + "/api/console\"");
        //paths.add("/server/" + server.id + "/api/console");
        //config.enableSimpleBroker(paths.toArray(new String[0]));

        //Logger.info("[Stomp] Registering server: " + server.id + " @ \"/server/" + server.id + "/api/server\"");
        //registry.addEndpoint("/server/" + server.id + "/api/server");
        //registry.addEndpoint("/server/" + server.id + "/api/server").withSockJS();
    }

    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new SubscriptionInterceptor());
    }
}