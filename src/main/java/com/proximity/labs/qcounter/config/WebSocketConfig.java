package com.proximity.labs.qcounter.config;

import com.proximity.labs.qcounter.component.CounterHandler;

import com.proximity.labs.qcounter.component.GuestCounterHandler;
import com.proximity.labs.qcounter.service.InQueueService;
import com.proximity.labs.qcounter.service.QueueService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	private static final Logger logger = Logger.getLogger(WebSocketConfig.class);

	private final QueueService queueService;
	private final InQueueService inQueueService;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	public WebSocketConfig(QueueService queueService, InQueueService inQueueService, ApplicationEventPublisher applicationEventPublisher) {
		this.queueService = queueService;
		this.inQueueService = inQueueService;
		this.applicationEventPublisher = applicationEventPublisher;
	}

	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new CounterHandler(queueService, inQueueService, applicationEventPublisher), "/owner/counter");
		registry.addHandler(new GuestCounterHandler(queueService, inQueueService), "/guest/counter");
	}

	
}