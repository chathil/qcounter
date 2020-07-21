package com.proximity.labs.qcounter.controllers;

import java.util.function.Function;

import com.proximity.labs.qcounter.annotation.CurrentUser;
import com.proximity.labs.qcounter.data.dto.request.NewQueueRequest;
import com.proximity.labs.qcounter.data.dto.response.NewQueueResponse;
import com.proximity.labs.qcounter.data.models.queue.Queue;
import com.proximity.labs.qcounter.data.models.user.User;
// import com.proximity.labs.qcounter.service.InQueueService;
import com.proximity.labs.qcounter.service.QueueService;
import com.proximity.labs.qcounter.service.QueueStatsService;
import com.proximity.labs.qcounter.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    // private final InQueueService inQueueService;
    private final QueueStatsService queueStatsService;

    @Autowired
    public QueueController(UserService userService, QueueService queueService, QueueStatsService queueStatsService) {
        this.userService = userService;
        this.queueService = queueService;
        // this.inQueueService = inQueueService;
        this.queueStatsService = queueStatsService;
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
        Queue newQ = queueService.createQueue(nQueueRequest).get();
        NewQueueResponse qResponse = new NewQueueResponse(newQ.getClientGeneratedId(), newQ.getName(),
                currentUser.getName(), currentUser.getId(), newQ.getDesc(), newQ.getMaxCapacity(),
                newQ.getIncrementBy(), newQ.getValidUntil().toInstant().toEpochMilli(), newQ.getContact(), newQ.getIsClosedQueue(),
                newQ.getQueueStats().getState(), "null");
        return ResponseEntity.ok(qResponse);
    }
}