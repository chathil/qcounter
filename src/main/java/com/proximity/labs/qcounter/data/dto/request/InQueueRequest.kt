package com.proximity.labs.qcounter.data.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(value = "InQueue Request DTO", description = "Payload to request all users inside a queue")
data class InQueueRequest(
        @ApiModelProperty(value = "queue id generated by client", required = true, allowableValues = "NonEmpty String, range[18, 19]", example = "KbFlP8A08y2k4Vp1XET", allowEmptyValue = false)
        @get:JsonProperty("queue_id")
        val queueId: String
)

@ApiModel(value = "Remove From In Queue Request DTO", description = "Paylod to request a user/ client removal from an in queue.")
data class RemoveFromInQueueRequest(
        @ApiModelProperty(value = "id of in queue to remove", required = true, allowableValues = "Unsigned Long")
        @get:JsonProperty("id")
        val id: Long,
        @ApiModelProperty(value = "queue id generated by client", required = true, allowableValues = "NonEmpty String, range[18, 19]", example = "KbFlP8A08y2k4Vp1XET", allowEmptyValue = false)
        @get:JsonProperty("queue_id")
        val queueId: String
)