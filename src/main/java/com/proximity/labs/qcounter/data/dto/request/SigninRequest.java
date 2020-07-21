package com.proximity.labs.qcounter.data.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Signin Request DTO", description = "Client's signin payload")
public class SigninRequest extends AuthRequest{}