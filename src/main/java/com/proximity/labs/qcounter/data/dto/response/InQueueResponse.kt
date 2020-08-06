package com.proximity.labs.qcounter.data.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(value = "In Queue Response DTO", description = "A model contains information about a user/ client in a queue")
data class InQueueResponse(
        @ApiModelProperty(value = "in queue table id")
        val id: Long,
        @ApiModelProperty(value = "user table id")
        @get:JsonProperty("user_id")
        val userId: Long,
        @ApiModelProperty(value = "user's queue number")
        @get:JsonProperty("queue_num")
        val queueNum: Int,
        @ApiModelProperty(value = "user's name in queue")
        val name: String,
        @ApiModelProperty(value = "user's contact number")
        val contact: String
)