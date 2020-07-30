package com.proximity.labs.qcounter.config;

import com.proximity.labs.qcounter.component.CounterSocketHandler;

import com.proximity.labs.qcounter.service.CounterSocketService;
import com.proximity.labs.qcounter.service.InQueueService;
import com.proximity.labs.qcounter.service.QueueService;
import com.proximity.labs.qcounter.utils.CounterSocketAtrr;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private static final Logger logger = Logger.getLogger(WebSocketConfig.class);
    /**
     * Fields to inject into CounterHandler class. Injection happens
     * here because constructor injection in preferable than field injection.
     */
    private final QueueService queueService;
    private final CounterSocketService counterSocketService;

    @Autowired
    public WebSocketConfig(QueueService queueService, InQueueService inQueueService, CounterSocketService counterSocketService) {
        this.queueService = queueService;
        this.counterSocketService = counterSocketService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new CounterSocketHandler(queueService, counterSocketService), "/counter/**/**").addInterceptors(queueIdInterceptor());
    }

    /**
     * Custom handshake interceptor to add queue id and target path
     * to websocket session before handshake.
     * target path determined what response the user will get.
     * "/" will be added if none found inorder to make the path uniform.
     *
     * @author proximity-labs
     */
    @Bean
    public HandshakeInterceptor queueIdInterceptor() {
        return new HandshakeInterceptor() {

            /**
             * {@inheritDoc}
             * Get the path from request, uniform it if necessary,
             * then add target path and queue id to attributes.
             * @param request
             * @param response
             * @param wsHandler
             * @param attributes
             * @return
             */
            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                String path = request.getURI().getPath();
                if (path.charAt(path.length() - 1) != '/')
                    path = path + "/";
                String pathSubString = path.substring(path.indexOf('/', 1), path.length() > 20 ? path.indexOf('/', 1) + 10 : path.length());
                String targetPath = pathSubString.substring(pathSubString.indexOf('/') + 1, pathSubString.lastIndexOf('/'));
                path = path.substring(0, path.lastIndexOf('/'));
                String queueId = path.substring(path.lastIndexOf('/') + 1);
                attributes.put(CounterSocketAtrr.TARGET_PATH.label, CounterSocketAtrr.valueOf(targetPath.toUpperCase()));
                attributes.put(CounterSocketAtrr.QUEUE_ID.label, queueId);
                return true;
            }

            @Override
            public void afterHandshake(org.springframework.http.server.ServerHttpRequest request, org.springframework.http.server.ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
                // Nothing to do after handshake
            }
        };
    }


}