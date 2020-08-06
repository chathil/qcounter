package com.proximity.labs.qcounter.controllers;

import com.proximity.labs.qcounter.annotation.CurrentUser;
import com.proximity.labs.qcounter.data.dto.request.JoinQueueRequest;
import com.proximity.labs.qcounter.data.dto.request.NewQueueRequest;
import com.proximity.labs.qcounter.data.dto.response.JoinQueueResponse;
import com.proximity.labs.qcounter.data.dto.response.QueueResponse;
import com.proximity.labs.qcounter.data.models.queue.InQueue;
import com.proximity.labs.qcounter.data.models.queue.Queue;
import com.proximity.labs.qcounter.data.models.queue.QueueStats;
import com.proximity.labs.qcounter.data.models.user.User;
import com.proximity.labs.qcounter.exception.AppException;
import com.proximity.labs.qcounter.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/queue")
@Api(tags = "Queue" ,value = "Queue Rest API. Defines queue operation related endpoints")
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
     * Create and return newly created queue
     *
     * @param currentUser current logged in user
     * @return @{@link ResponseEntity} of {@link QueueResponse} that contains data about newly created queue
     */
    @ApiOperation(value = "Create a new queue and return the created queue along with it stats data from queue_stats table.")
    @PostMapping
    public ResponseEntity<QueueResponse> newQueue(@CurrentUser User currentUser, @RequestBody NewQueueRequest nQueueRequest) {
        if (currentUser.getMyQueues().size() > 1)
            throw new AppException("You have reached max queue you can create. which is two");

        return queueService.createQueueAndPersist(currentUser, nQueueRequest).map(newQ -> ResponseEntity.ok(new QueueResponse(newQ.getClientGeneratedId(), newQ.getName(),
                currentUser.getName(), currentUser.getId(), newQ.getDesc(), newQ.getMaxCapacity(),
                newQ.getIncrementBy(), newQ.getValidUntil().toInstant().toEpochMilli(), newQ.getContact(),
                newQ.getIsClosedQueue(), newQ.getQueueStats().getState(), newQ.getQueueStats().getCurrentQueue(), newQ.getQueueStats().getCurrentInQueue(), "null"))).orElseThrow(() -> new AppException(
                String.format("Could't create [%s] due to internal error", nQueueRequest.getName())));
    }

    /**
     * Return all guest readable details about this queue,
     * currentQueue, and state. NOT SECURED.
     * user will probably navigate here with qr-code
     * currently returning github pages but later will json
     *
     * @param queueId to get the details
     * @return @{@link ResponseEntity} of {@link QueueResponse} that contains data about queue
     */
    @ApiOperation(value = "Return all guest readable detail about this queue, this route is meant for unauthorized guest. Not secured by default.")
    @GetMapping(value = "/guest/{queue_id}")
    public ResponseEntity<QueueResponse> guestQueueDetail(@PathVariable("queue_id") String queueId) {
        return queueService.findFirstByClientGeneratedId(queueId).map(queue -> ResponseEntity.ok(new QueueResponse(queue.getClientGeneratedId(), queue.getName(),
                queue.getOwner().getName(), queue.getOwner().getId(), queue.getDesc(), queue.getMaxCapacity(),
                queue.getIncrementBy(), queue.getValidUntil().toInstant().toEpochMilli(), queue.getContact(),
                queue.getIsClosedQueue(), queue.getQueueStats().getState(), queue.getQueueStats().getCurrentQueue(), queue.getQueueStats().getCurrentInQueue(), "null"))).orElseThrow(() -> new AppException(
                String.format("Could't find [%s] due to internal error", queueId)));
    }

    /**
     * Find a queue and join.
     * if succeed it will return {@link JoinQueueResponse}
     * if no queue found it will throw general internal error.
     * if you're already join you're already join error will be thrown.
     * if you've reached max queue you can join, limit error will be thrown
     *
     * @param currentUser      current authenticated user in session
     * @param joinQueueRequest information needed inorder to join a queue
     * @return {@link ResponseEntity<JoinQueueResponse>}
     */
    @ApiOperation(value = "Join a queue, if succeed return all information about a queue you just joined")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Success", response = JoinQueueResponse.class),
                    @ApiResponse(code = 500, message = "Already joined", response = com.proximity.labs.qcounter.data.dto.response.ApiResponse.class),
                    @ApiResponse(code = 500, message = "Limit reached", response = com.proximity.labs.qcounter.data.dto.response.ApiResponse.class)
            }
    )
    @PostMapping(value = "/join")
    public ResponseEntity<JoinQueueResponse> joinQueue(@CurrentUser User currentUser, @RequestBody JoinQueueRequest joinQueueRequest) {
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

    @GetMapping(value = "/search")
    public ResponseEntity searchQueue() {
        return ResponseEntity.ok(null);
    }
}