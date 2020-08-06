/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.proximity.labs.qcounter.data.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "Common API Response", description = "A default API response used in multiple endpoints")
public class ApiResponse {

    @ApiModelProperty(value = "contains message about the operation")
    private final String data;
    @ApiModelProperty(value = "true if an operation is performed successfully, false otherwise")
    private final Boolean success;
    @ApiModelProperty(value = "operation time")
    private final String timestamp;
    @ApiModelProperty(value = "cause of the operation")
    private final String cause;
    @ApiModelProperty(value = "operation's enpoint")
    private final String path;

    public ApiResponse(Boolean success, String data, String cause, String path) {
        this.timestamp = Instant.now().toString();
        this.data = data;
        this.success = success;
        this.cause = cause;
        this.path = path;
    }

    public ApiResponse(Boolean success, String data) {
        this.timestamp = Instant.now().toString();
        this.data = data;
        this.success = success;
        this.cause = null;
        this.path = null;
    }

    public String getData() {
        return data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getCause() {
        return cause;
    }

    public String getPath() {
        return path;
    }
}
