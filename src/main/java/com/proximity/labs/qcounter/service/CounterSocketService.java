package com.proximity.labs.qcounter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proximity.labs.qcounter.data.dto.response.ApiResponse;
import com.proximity.labs.qcounter.data.dto.response.ControlCounterResponse;
import com.proximity.labs.qcounter.data.dto.response.HomeCounterResponse;
import com.proximity.labs.qcounter.data.dto.response.JoinQueueCounterResponse;
import com.proximity.labs.qcounter.data.models.queue.InQueue;
import com.proximity.labs.qcounter.data.models.queue.Queue;
import com.proximity.labs.qcounter.data.models.queue.QueueState;
import com.proximity.labs.qcounter.data.models.queue.QueueStats;
import com.proximity.labs.qcounter.data.models.user.User;
import com.proximity.labs.qcounter.utils.CounterSocketAtrr;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CounterSocketService {
    private final InQueueService inQueueService;
    private final QueueService queueService;

    /**
     * Store all sessions from "join" target path in a hashmap. The key is queue id.
     */
    HashMap<String, Set<WebSocketSession>> joinQueueSessions = new HashMap<>();

    /**
     * Store all sessions from "home" target path in a hashmap. The key is queue id.
     */
    HashMap<String, Set<WebSocketSession>> homeSessions = new HashMap<>();

    /**
     * Store all sessions from "control" target path in a hashmap. The key is queue id.
     */
    HashMap<String, Set<WebSocketSession>> controlSessions = new HashMap<>();

    public CounterSocketService(InQueueService inQueueService, QueueService queueService) {
        this.queueService = queueService;
        this.inQueueService = inQueueService;
    }

    public HashMap<String, Set<WebSocketSession>> getJoinQueueSessions() {
        return joinQueueSessions;
    }

    public void setJoinQueueSessions(HashMap<String, Set<WebSocketSession>> joinQueueSessions) {
        this.joinQueueSessions = joinQueueSessions;
    }

    public HashMap<String, Set<WebSocketSession>> getHomeSessions() {
        return homeSessions;
    }

    public void setHomeSessions(HashMap<String, Set<WebSocketSession>> homeSessions) {
        this.homeSessions = homeSessions;
    }

    public HashMap<String, Set<WebSocketSession>> getControlSessions() {
        return controlSessions;
    }

    public void setControlSessions(HashMap<String, Set<WebSocketSession>> controlSessions) {
        this.controlSessions = controlSessions;
    }

    /**
     * Gets value from the incoming message and convert it to POJO with @{@link ObjectMapper}
     * then perform operation based on the extracted value and call @notifyChanges to broadcast it to all related subscribers.
     *
     * @param queue    the queue where the operation will be performed
     * @param incoming the incoming message from client
     * @param session  the queue author's session
     */
    public void performChanges(Queue queue, TextMessage incoming, WebSocketSession session) {
        Map<String, String> value = null;
        try {
            value = new ObjectMapper().readValue(incoming.getPayload(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            sendErrorAndCloseConnection(new ApiResponse(false, "Cannot parse the message."), session);
        }

        assert value != null;
        switch (CounterSocketAtrr.valueOf(value.get(CounterSocketAtrr.OPERATION.label).toUpperCase())) {
            case INCREMENT -> {
                if (queue.getQueueStats().getState() == QueueState.RUNNING) {
                    inQueueService.increment(queue);
                    notifyChanges(queue);
                } else
                    sendError(new ApiResponse(false, String.format("Queue with id %s is in %s state", queue.getClientGeneratedId(), queue.getQueueStats().getState())), session);
            }
            case DECREMENT -> {
                if (queue.getQueueStats().getState() == QueueState.RUNNING) {
                    inQueueService.decrement(queue);
                    notifyChanges(queue);
                } else
                    sendError(new ApiResponse(false, String.format("Queue with id %s is in %s state", queue.getClientGeneratedId(), queue.getQueueStats().getState())), session);
            }
            case PAUSE -> {
                queue.getQueueStats().setState(QueueState.PAUSED);
                queueService.save(queue);
                notifyChanges(queue);
            }
            case RESUME -> {
                queue.getQueueStats().setState(QueueState.RUNNING);
                queueService.save(queue);
                notifyChanges(queue);
            }
        }
    }

    /**
     * Notifies all queue subscribers about changes that happens
     * to the queue across session group. for homeSessions, whenever
     * the owner perform changes it will cause the home's nextQueueCounter
     * to be null because the queue that's being observed is different that
     * the one that changes. To fix that. we will perform another database query
     * based on what the session is subscribed to. if the session doesn's
     * subscribed to anything then ignore.
     *
     * @param queue target queue where the operation is already performed.
     */
    public void notifyChanges(Queue queue) {

        QueueStats qStats = queue.getQueueStats();
        String joinMessage = new JoinQueueCounterResponse(queue.getClientGeneratedId(), qStats.getCurrentQueue(), qStats.getCurrentInQueue()).toString();
        Map<User, InQueue> userInQueueMap = inQueueService.findByQueueId(queue.getId()).stream().collect(Collectors.toMap(InQueue::getUser, Function.identity()));

        List<Pair<Long, String>> current = inQueueService.getCurrent(queue);
        List<Pair<Long, String>> prev = inQueueService.getPrev(queue);
        List<Pair<Long, String>> next = inQueueService.getNext(queue);

        joinQueueSessions
                .getOrDefault(queue.getClientGeneratedId(), Set.of())
                .forEach(session -> {
                    try {
                        session.sendMessage(new TextMessage(joinMessage));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        homeSessions
                .getOrDefault(queue.getClientGeneratedId(), Set.of())
                .forEach(session -> {
                    User user = (User) ((Authentication) Objects.requireNonNull(session.getPrincipal())).getPrincipal();
                    InQueue inQ = userInQueueMap.get(user);
                    HomeCounterResponse homeCounterResponse = new HomeCounterResponse();
                    if (inQ != null)
                        homeCounterResponse.setNextQueueCounter(queue.getClientGeneratedId(), inQ.getQueueNum(), queue.getQueueStats().getCurrentQueue(), queue.getQueueStats().getState());
                    else {
                        Optional<Queue> optSubscribedQ = queueService.findFirstByClientGeneratedId(session.getAttributes().getOrDefault(CounterSocketAtrr.QUEUE_ID.label, "").toString());
                        optSubscribedQ.ifPresent(subscribedQ -> {
                            Optional<InQueue> userInQueue = inQueueService.findUserInQueue(subscribedQ.getId(), user.getId());
                            userInQueue.ifPresent(value -> homeCounterResponse.setNextQueueCounter(subscribedQ.getClientGeneratedId(), value.getQueueNum(), subscribedQ.getQueueStats().getCurrentQueue(), subscribedQ.getQueueStats().getState()));
                        });
                    }
                    queueService.findByUserOwnerId(user.getId()).forEach(myQ -> homeCounterResponse.setMyQueues(myQ.getClientGeneratedId(), myQ.getName(), myQ.getIncrementBy(), myQ.getQueueStats().getCurrentQueue(), myQ.getQueueStats().getCurrentInQueue()));
                    try {
                        session.sendMessage(new TextMessage(homeCounterResponse.toString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        controlSessions.getOrDefault(queue.getClientGeneratedId(), Set.of()).forEach(session -> {
            try {
                session.sendMessage(new TextMessage(notifyControlSession(queue, prev, current, next)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Call methods to  generate initial message which contains queue status for different path.
     * message structure will be different based on target path.
     * this method also decides the session grouping.
     *
     * @param session    target to send message to
     * @param queue      a queue from session attributes
     * @param targetPath session's target path
     * @return may return String or empty String
     */
    public Optional<String> initialBroadcastMessage(WebSocketSession session, Queue queue, CounterSocketAtrr targetPath) {
        User user = (User) ((Authentication) Objects.requireNonNull(session.getPrincipal())).getPrincipal();
        switch (targetPath) {
            case JOIN -> {
                putOrAdd(session, queue, joinQueueSessions);
                return Optional.ofNullable(new JoinQueueCounterResponse(queue.getClientGeneratedId(), queue.getQueueStats().getCurrentQueue(), queue.getQueueStats().getCurrentInQueue()).toString());
            }
            case HOME -> {
                if (queue != null)
                    putOrAdd(session, queue, homeSessions);
                user.getMyQueues().forEach(q -> putOrAdd(session, q, homeSessions));
                return Optional.ofNullable(notifyHomeSession(queue, user, user.getMyQueues()));
            }
            case CONTROL -> {
                if (verifyOwnership(session.getPrincipal(), queue)) {
                    putOrAdd(session, queue, controlSessions);
                    return Optional.ofNullable(notifyControlSession(queue, inQueueService.getPrev(queue), inQueueService.getCurrent(queue), inQueueService.getNext(queue)));
                } else
                    sendErrorAndCloseConnection(new ApiResponse(false, String.format("Queue with id %s does not belong to %s", queue.getClientGeneratedId(), session.getPrincipal().getName())), session);
            }
            default -> sendErrorAndCloseConnection(new ApiResponse(false, "Unknown operation"), session);
        }
        return Optional.empty();
    }

    /**
     * Generate initial message for a session that sets CONTROL as target path.
     * if session belongs to a user with queues. then also assign this session to all those queues.
     *
     * @param queue    a queue from session attributes
     * @param user     target user, the in queue owner
     * @param myQueues queues created by the user
     * @return json String of @{@link HomeCounterResponse}
     */
    public String notifyHomeSession(Queue queue, User user, Set<Queue> myQueues) {
        HomeCounterResponse homeCounterResponse = new HomeCounterResponse();
        if (queue != null) {
            Optional<InQueue> inQ = inQueueService.findUserInQueue(queue.getId(), user.getId());
            inQ.ifPresent(inQueue -> homeCounterResponse.setNextQueueCounter(queue.getClientGeneratedId(), inQueue.getQueueNum(), queue.getQueueStats().getCurrentQueue(), queue.getQueueStats().getState()));
        }
        myQueues.forEach(myQ -> homeCounterResponse.setMyQueues(myQ.getClientGeneratedId(), myQ.getName(), myQ.getIncrementBy(), myQ.getQueueStats().getCurrentQueue(), myQ.getQueueStats().getCurrentInQueue()));
        return homeCounterResponse.toString();
    }

    /**
     * Generate initial message & changes message for a session
     * that sets CONTROL as target path.
     *
     * @param queue   where the session is subscribed to
     * @param prev    previous user in line/ queue.
     * @param current user that's being served
     * @param next    next user in line/ queue.
     * @return json String of @{@link ControlCounterResponse}
     */
    public String notifyControlSession(Queue queue, List<Pair<Long, String>> prev, List<Pair<Long, String>> current, List<Pair<Long, String>> next) {
        QueueStats qStats = queue.getQueueStats();
        ControlCounterResponse controlCounterResponse =
                new ControlCounterResponse(
                        queue.getClientGeneratedId(),
                        qStats.getState(),
                        qStats.getCurrentQueue(),
                        qStats.getCurrentInQueue(),
                        queue.getMaxCapacity());
        prev.forEach(user -> controlCounterResponse.addPrev(user.getFirst(), user.getSecond()));
        next.forEach(user -> controlCounterResponse.addNext(user.getFirst(), user.getSecond()));
        current.forEach(user -> controlCounterResponse.addCurrent(user.getFirst(), user.getSecond()));
        return controlCounterResponse.toString();
    }

    /**
     * If there's no one subscribed to a queue inside specified group then create a new Set and add it.
     * If there is, then simply add the session to that Set of @{@link WebSocketSession}
     *
     * @param session  to add
     * @param queue    to subscribe to
     * @param sessions Set to add session to.
     */
    public void putOrAdd(WebSocketSession session, Queue queue, HashMap<String, Set<WebSocketSession>> sessions) {
        Set<WebSocketSession> subSessions = sessions.get(queue.getClientGeneratedId());
        if (subSessions == null) {
            Set<WebSocketSession> newSession = new CopyOnWriteArraySet<>();
            newSession.add(session);
            sessions.put(queue.getClientGeneratedId(), newSession);
        } else {
            subSessions.add(session);
        }
    }

    /**
     * Send error message explaining the error and then close the connection
     *
     * @param apiResponse error format
     * @param session     session to send error to.
     */
    public void sendErrorAndCloseConnection(ApiResponse apiResponse, WebSocketSession session) {
        try {
            session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsBytes(apiResponse)));
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send error message explaining the error but keep the connection
     *
     * @param apiResponse error format
     * @param session     session to send error to.
     */
    public void sendError(ApiResponse apiResponse, WebSocketSession session) {
        try {
            session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsBytes(apiResponse)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert session's principan into @{@link Authentication} principal then into @{@link User}.
     * check if the queue belongs to that user.
     *
     * @param principal @{@link WebSocketSession}'s principal
     * @param queue     to verify ownership
     * @return true if the owner verified, false otherwise.
     */
    public boolean verifyOwnership(Principal principal, Queue queue) {
        User user = (User) ((Authentication) principal).getPrincipal();
        return user.getId().equals(queue.getOwner().getId());
    }

}
