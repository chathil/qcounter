package com.proximity.labs.qcounter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages.simpDestMatchers("/secured/**").permitAll().anyMessage().permitAll();
    }

    /**
     * Set this to false on production env.
     */
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}