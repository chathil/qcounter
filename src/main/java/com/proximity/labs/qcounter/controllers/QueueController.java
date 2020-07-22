package com.proximity.labs.qcounter.controllers;

import java.util.function.Function;

import com.google.zxing.qrcode.encoder.QRCode;
import com.proximity.labs.qcounter.annotation.CurrentUser;
import com.proximity.labs.qcounter.data.dto.request.CounterRequest;
import com.proximity.labs.qcounter.data.dto.request.JoinQueueRequest;
import com.proximity.labs.qcounter.data.dto.request.NewQueueRequest;
import com.proximity.labs.qcounter.data.dto.response.JoinQueueResponse;
import com.proximity.labs.qcounter.data.dto.response.NewQueueResponse;
import com.proximity.labs.qcounter.data.dto.response.UserCounterResponse;
import com.proximity.labs.qcounter.data.models.queue.InQueue;
import com.proximity.labs.qcounter.data.models.queue.Queue;
import com.proximity.labs.qcounter.data.models.queue.QueueStats;
import com.proximity.labs.qcounter.data.models.user.User;
import com.proximity.labs.qcounter.exception.AppException;
import com.proximity.labs.qcounter.exception.UserRegistrationException;
import com.proximity.labs.qcounter.service.InQueueService;
import com.proximity.labs.qcounter.service.QrCodeService;
import com.proximity.labs.qcounter.service.QueueService;
import com.proximity.labs.qcounter.service.QueueStatsService;
import com.proximity.labs.qcounter.service.UserService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/queue")
public class QueueController {

    private final UserService userService;
    private final QueueService queueService;
    private final InQueueService inQueueService;
    private final QueueStatsService queueStatsService;
    private final QrCodeService qrCodeService;
    private final String rootUrl;
    private static final Logger logger = Logger.getLogger(QueueController.class);

    @Autowired
    public QueueController(UserService userService, QueueService queueService, QueueStatsService queueStatsService,
            InQueueService inQueueService, QrCodeService qrCodeService, @Value("${app.root.url}") String rootUrl) {
        this.userService = userService;
        this.queueService = queueService;
        this.inQueueService = inQueueService;
        this.queueStatsService = queueStatsService;
        this.qrCodeService = qrCodeService;
        this.rootUrl = rootUrl;
    }

    /**
     * Create new queue doc by @chathil
     * 
     * @param currentUser
     * @return @ResponseEntity
     */
    @ApiOperation(value = "Create a new queue and return the created queue without data from queue_stats table.")
    @PostMapping
    public ResponseEntity newQueue(@CurrentUser User currentUser, @RequestBody NewQueueRequest nQueueRequest) {
        return queueService.createQueueAndPersist(currentUser ,nQueueRequest).map(newQ -> {
            return ResponseEntity.ok(new NewQueueResponse(newQ.getClientGeneratedId(), newQ.getName(),
                    currentUser.getName(), currentUser.getId(), newQ.getDesc(), newQ.getMaxCapacity(),
                    newQ.getIncrementBy(), newQ.getValidUntil().toInstant().toEpochMilli(), newQ.getContact(),
                    newQ.getIsClosedQueue(), newQ.getQueueStats().getState(), "null"));
        }).orElseThrow(() -> new AppException(
                String.format("Could't create [%s] due to internal error", nQueueRequest.getName())));
    }

    /**
     * 
     * Return all guest readable details about this queue without currentInQueue,
     * currentQueue, and state. NOT SECURED. doc by @chathil
     * 
     * @param queueId
     * @return
     */
    @ApiOperation(value = "Return all guest readable detail about this queue, this route is meant for unauthorized guest. Not secured by default.")
    @GetMapping(value = "/guest/{queue_id}")
    public ResponseEntity guestQueueDetail(@PathVariable("queue_id") String queueId) {
        logger.info(queueId);
        return ResponseEntity.ok(rootUrl);
    }

    /**
     * a websocket that returns realtime currentInQueue, currentQueue, and state.
     * for guest.
     * 
     * @return
     */
    @MessageMapping("/secured/counter")
    @SendTo("/secured/topic/counter")
    public ResponseEntity guestCounter(CounterRequest counterRequest) {
        logger.info(counterRequest.getQueueId());
        return ResponseEntity.ok(counterRequest.getQueueId());
    }

    /**
     * a websocket that returns realtime queue counter for queue owner.
     * 
     * @return
     */
    public ResponseEntity ownerCounter() {
        return ResponseEntity.ok("");
    }

    /**
     * Join a queue
     */
    @PostMapping(value = "/join")
    public ResponseEntity joinQueue(@CurrentUser User currentUser, @RequestBody JoinQueueRequest joinQueueRequest) {
        return inQueueService.joinQueueAndPersist(currentUser, joinQueueRequest).map(pair -> {
            Queue q = pair.getFirst();
            InQueue inQ = pair.getSecond();
            User qOwner = q.getOwner();
            QueueStats qStats = q.getQueueStats();
            return ResponseEntity.ok(new JoinQueueResponse(q.getClientGeneratedId(), q.getName(), q.getDesc(),
                    q.getContact(), q.getMaxCapacity(), qStats.getCurrentQueue(), q.getIncrementBy(),
                    q.getValidUntil().getTime(), q.getIsClosedQueue(), qStats.getState(), inQ.getQueueNum(),
                    inQ.getName(), inQ.getContact(), qOwner.getName(), qOwner.getId()));
        }).orElseThrow(() -> new AppException(joinQueueRequest.getFullName() + " cannot join due to internal error"));
    }
}