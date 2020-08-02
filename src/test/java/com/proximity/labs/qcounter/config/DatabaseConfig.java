package com.proximity.labs.qcounter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@ConditionalOnProperty(value="otpConfig", havingValue="test")
public class DatabaseConfig {

}
