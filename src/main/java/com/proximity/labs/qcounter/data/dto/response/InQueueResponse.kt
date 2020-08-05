package com.proximity.labs.qcounter.data.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class InQueueResponse(
        val id: Long,
        @get:JsonProperty("user_id")
        val userId: Long,
        val name: String,
        val contact: String
)