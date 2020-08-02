package com.proximity.labs.qcounter.controllers;

import com.proximity.labs.qcounter.annotation.CurrentUser;
import com.proximity.labs.qcounter.data.dto.request.JoinQueueRequest;
import com.proximity.labs.qcounter.data.dto.request.NewQueueRequest;
import com.proximity.labs.qcounter.data.dto.response.JoinQueueResponse;
import com.proximity.labs.qcounter.data.dto.response.NewQueueResponse;
import com.proximity.labs.qcounter.data.models.queue.InQueue;
import com.proximity.labs.qcounter.data.models.queue.Queue;
import com.proximity.labs.qcounter.data.models.queue.QueueStats;
import com.proximity.labs.qcounter.data.models.user.User;
import com.proximity.labs.qcounter.exception.AppException;
import com.proximity.labs.qcounter.service.*;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
        if (currentUser.getMyQueues().size() > 1)
            throw new AppException("You have reached max queue you can create. which is two");

        return queueService.createQueueAndPersist(currentUser, nQueueRequest).map(newQ -> ResponseEntity.ok(new NewQueueResponse(newQ.getClientGeneratedId(), newQ.getName(),
                currentUser.getName(), currentUser.getId(), newQ.getDesc(), newQ.getMaxCapacity(),
                newQ.getIncrementBy(), newQ.getValidUntil().toInstant().toEpochMilli(), newQ.getContact(),
                newQ.getIsClosedQueue(), newQ.getQueueStats().getState(), "null"))).orElseThrow(() -> new AppException(
                String.format("Could't create [%s] due to internal error", nQueueRequest.getName())));
    }

    /**
     * Return all guest readable details about this queue without currentInQueue,
     * currentQueue, and state. NOT SECURED.
     * user will probably navigate here with qr-code
     * currently returning github pages but later will json
     *
     * @param queueId
     * @return all guest readable details about this queue without currentInQueue
     */
    @ApiOperation(value = "Return all guest readable detail about this queue, this route is meant for unauthorized guest. Not secured by default.")
    @GetMapping(value = "/guest/{queue_id}")
    public ResponseEntity guestQueueDetail(@PathVariable("queue_id") String queueId) {
        logger.info(queueId);
        return ResponseEntity.ok(rootUrl);
    }

    /**
     * Join a queue
     */
    @PostMapping(value = "/join")
    public ResponseEntity joinQueue(@CurrentUser User currentUser, @RequestBody JoinQueueRequest joinQueueRequest) {
        if (currentUser.getQueues().size() > 1)
            throw new AppException("You have reached max queue you can join. which is two");
        Queue qToJoin = queueService.findFirstByClientGeneratedId(joinQueueRequest.getQueueId())
                .orElseThrow(() -> new AppException(String.format("Queue with id %s not exist", joinQueueRequest.getQueueId())));
        if (inQueueService.findUserInQueue(qToJoin.getId(), currentUser.getId()).isPresent())
            throw new AppException(String.format("You already joined queue with id %s", qToJoin.getClientGeneratedId()));
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