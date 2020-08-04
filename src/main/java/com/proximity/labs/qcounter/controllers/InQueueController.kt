package com.proximity.labs.qcounter.controllers

import com.proximity.labs.qcounter.data.dto.request.InQueueRequest
import com.proximity.labs.qcounter.data.dto.response.InQueueResponse
import com.proximity.labs.qcounter.exception.ResourceNotFoundException
import com.proximity.labs.qcounter.service.InQueueService
import com.proximity.labs.qcounter.service.QueueService
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody


@RestController
@RequestMapping("/in_queue")
class InQueueController @Autowired constructor(private val queueService: QueueService, private val inQueueService: InQueueService) {

    @GetMapping
    fun inQueues(@RequestBody inQueueRequest: InQueueRequest): ResponseEntity<List<InQueueResponse>> {
       return queueService.findFirstByClientGeneratedId(inQueueRequest.queueId).map {
           ResponseEntity.ok(inQueueService.findByQueueId(it.id).map { inQ ->
               InQueueResponse(inQ.id, inQ.user.id, inQ.name, inQ.contact)
           })
       }.orElseThrow { ResourceNotFoundException("Queue", "queue_id", inQueueRequest.queueId) }
    }

    companion object {
        private val logger = Logger.getLogger(InQueueController::class.java)
    }
}