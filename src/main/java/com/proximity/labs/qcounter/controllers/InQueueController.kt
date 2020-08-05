package com.proximity.labs.qcounter.controllers

import com.proximity.labs.qcounter.annotation.CurrentUser
import com.proximity.labs.qcounter.data.dto.request.InQueueRequest
import com.proximity.labs.qcounter.data.dto.request.JoinQueueRequest
import com.proximity.labs.qcounter.data.dto.response.InQueueResponse
import com.proximity.labs.qcounter.data.models.user.User
import com.proximity.labs.qcounter.exception.AppException
import com.proximity.labs.qcounter.exception.ResourceNotFoundException
import com.proximity.labs.qcounter.service.InQueueService
import com.proximity.labs.qcounter.service.QueueService
import org.springframework.beans.factory.annotation.Autowired
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/in_queue")
class InQueueController @Autowired constructor(private val queueService: QueueService, private val inQueueService: InQueueService) {

    @GetMapping
    fun inQueues(@CurrentUser user: User, @RequestBody inQueueRequest: InQueueRequest): ResponseEntity<List<InQueueResponse>> {
        return queueService.findFirstByClientGeneratedId(inQueueRequest.queueId).map {
            println((it.owner == user).toString() + " it should be false")
            if (it.owner != user)
                throw AppException("Queue with id ${inQueueRequest.queueId} doesn't belongs to ${user.name}")
            ResponseEntity.ok(inQueueService.findByQueueId(it.id).map { inQ ->
                InQueueResponse(inQ.id, inQ.user.id, inQ.name, inQ.contact)
            })
        }.orElseThrow { ResourceNotFoundException("Queue", "queue_id", inQueueRequest.queueId) }
    }

    @PostMapping
    fun addToQueue(@CurrentUser user: User, @RequestBody joinQueueRequest: JoinQueueRequest): ResponseEntity<InQueueResponse> {
        return inQueueService.addToQueueAndPersist(user, joinQueueRequest).map {
            ResponseEntity.ok(InQueueResponse(it.second.id, -1L, it.second.name, it.second.contact))
        }.orElseThrow { ResourceNotFoundException("Queue", "queue_id", joinQueueRequest.queueId) }
    }

    companion object {
        private val logger = Logger.getLogger(InQueueController::class.java)
    }
}