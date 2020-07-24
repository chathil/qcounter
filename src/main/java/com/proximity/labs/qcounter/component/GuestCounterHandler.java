package com.proximity.labs.qcounter.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proximity.labs.qcounter.data.dto.request.CounterRequest;
import com.proximity.labs.qcounter.event.OnOwnerPerformChanges;
import com.proximity.labs.qcounter.service.InQueueService;
import com.proximity.labs.qcounter.service.QueueService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class GuestCounterHandler extends TextWebSocketHandler implements ApplicationListener<OnOwnerPerformChanges> {
    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = Logger.getLogger(GuestCounterHandler.class);

    private final QueueService queueService;
    private final InQueueService inQueueService;

    public GuestCounterHandler(QueueService queueService, InQueueService inQueueService) {
        this.queueService = queueService;
        this.inQueueService = inQueueService;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {
        CounterRequest value = mapper.readValue(message.getPayload(), CounterRequest.class);
        for (WebSocketSession webSocketSession : sessions) {
            webSocketSession.sendMessage(new TextMessage("hellow from guest"));
        }
        logger.info("session size in handleTextMessage " + sessions.size());
        logger.info(value.getOperation() + " " + value.getQueueId());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // the messages will be broadcasted to all users.

        logger.info(session.getPrincipal().getName());
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    @Override
    public void onApplicationEvent(OnOwnerPerformChanges event) {
        logger.info("from children " + event.getQueueId());
        try {
            broadcastToSubscribers();
            logger.info("called try");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("called error");

        }
    }

    private void broadcastToSubscribers() throws IOException {
        logger.info("called here");
        logger.info("session size in broad " + sessions.size());
        for (WebSocketSession webSocketSession : sessions) {
            logger.info("inside for " + webSocketSession.getPrincipal().getName());
            webSocketSession.sendMessage(new TextMessage("message"));
        }
    }


}