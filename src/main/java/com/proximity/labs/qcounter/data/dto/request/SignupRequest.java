package com.proximity.labs.qcounter.data.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Signup Request DTO", description = "The data required from user in order to register")
public class SignupRequest extends AuthRequest {

    @ApiModelProperty(value = "Full name of the user, this is not unique", required = true, allowableValues = "NonEmpty String")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}