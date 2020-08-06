package com.proximity.labs.qcounter.data.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class InQueueRequest(
        @get:JsonProperty("queue_id")
        val queueId: String
)

data class RemoveFromInQueueRequest(
        @get:JsonProperty("id")
        val id: Long,
        @get:JsonProperty("queue_id")
        val queueId: String
)