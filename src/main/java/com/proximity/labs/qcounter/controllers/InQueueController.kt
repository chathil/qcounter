package com.proximity.labs.qcounter.controllers

import com.proximity.labs.qcounter.annotation.CurrentUser
import com.proximity.labs.qcounter.data.dto.request.InQueueRequest
import com.proximity.labs.qcounter.data.dto.request.JoinQueueRequest
import com.proximity.labs.qcounter.data.dto.request.RemoveFromInQueueRequest
import com.proximity.labs.qcounter.data.dto.response.ApiResponse
import com.proximity.labs.qcounter.data.dto.response.InQueueResponse
import com.proximity.labs.qcounter.data.models.user.User
import com.proximity.labs.qcounter.exception.AppException
import com.proximity.labs.qcounter.exception.ResourceNotFoundException
import com.proximity.labs.qcounter.service.InQueueService
import com.proximity.labs.qcounter.service.QueueService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/in_queue")
@Api(tags = ["Manage User in Queue", "Manage Queue"], value = "InQueue Rest API. Defines endpoint to manage users/ clients inside a queue." +
        "Most of the operations here can only be performed by a queue owner.")
class InQueueController @Autowired constructor(private val queueService: QueueService, private val inQueueService: InQueueService) {

    /**
     * Get all users/ client that's currently inside the specified queue.
     * This endpoint can only be hit by a queue owner.
     * an @AppException with queue doesnt belong message will be thrown if
     * this queue doesn't belong to the currently authenticated user and
     * @ResourceNotFoundException will be thrown if queue with the specified
     * id is not found.
     *
     * @param inQueueRequest contains a queue id to get users/ clients from.
     * @return List or users/ clients inside a queue
     * @author abdul chathil
     */
    @ApiOperation(value = "Returns all users/ clients that's currently inside the specified queue." +
            "This endpoint can only be hit by a queue owner.")
    @GetMapping
    fun inQueues(@CurrentUser user: User, @RequestBody inQueueRequest: InQueueRequest): ResponseEntity<List<InQueueResponse>> {
        return queueService.findFirstByClientGeneratedId(inQueueRequest.queueId).map {
            if (it.owner != user)
                throw AppException("Queue with id ${inQueueRequest.queueId} doesn't belongs to ${user.name}")
            ResponseEntity.ok(inQueueService.findByQueueId(it.id).map { inQ ->
                InQueueResponse(inQ.id, inQ.user.id, inQ.queueNum, inQ.name, inQ.contact)
            })
        }.orElseThrow { ResourceNotFoundException("Queue", "queue_id", inQueueRequest.queueId) }
    }

    /**
     * Manually add a client into a queue.
     * The userId is set to -1L because there is no user is associated with it.
     * an @AppException with "No such queue" message or @ResourceNotFoundException
     * will be thrown if there's no queue found with the specified id.
     * another @AppException with "Queue with id %s doesn't belongs to %s"
     * will be thrown if the specified queue doesn't belongs to currently authenticated user.
     * This endpoint can only be hit by a queue owner.
     *
     * @param joinQueueRequest contains new value to be added to the database
     * @return a newly added client with userId set to null
     * @author Abdul Chathil
     */
    @ApiOperation(value = "Add a client to a queue, if succeed will return newly created client with userId set to -1L" +
            "because no registered user is associated with it. Because of that client added to a queue this way will not" +
            "be notify about queue changes via websocket. This endpoint can only be hit by a queue owner.")
    @PostMapping
    fun addToInQueue(@CurrentUser user: User, @RequestBody joinQueueRequest: JoinQueueRequest): ResponseEntity<InQueueResponse> {
        return inQueueService.addToQueueAndPersist(user, joinQueueRequest).map {
            ResponseEntity.ok(InQueueResponse(it.second.id, -1L, it.second.queueNum, it.second.name, it.second.contact))
        }.orElseThrow { ResourceNotFoundException("Queue", "queue_id", joinQueueRequest.queueId) }
    }

    /**
     * Remove an unwanted user from a queue
     * an @ResourceNotFoundException will be thrown if there's no queue found with the specified id
     * and @AppException with "Queue with id %s doesn't belongs to %s"
     * will be thrown if the specified queue doesn't belongs to currently authenticated user.
     * This endpoint can only be hit by a queue owner.
     *
     * @param removeFromInQueueRequest contains in_queue_id and queue__client_generate_id to remove
     * @author Abdul Chathil
     */
    @ApiOperation(value = "Remove an unwanted user from a queue." +
            "If succeed or no record found with the specified id, will return success message." +
            "This endpoint can only be hit by a queue owner.")
    @DeleteMapping
    fun remoteFromInQueue(@CurrentUser user: User, @RequestBody removeFromInQueueRequest: RemoveFromInQueueRequest): ResponseEntity<ApiResponse> {
        inQueueService.removeFromInQueue(user, removeFromInQueueRequest)
        return ResponseEntity.of(Optional.of(ApiResponse(true, "User with id ${removeFromInQueueRequest.id} removed")))
    }

    companion object {
        private val logger = Logger.getLogger(InQueueController::class.java)
    }
}