package com.proximity.labs.qcounter.advice;



import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class QueueControllerAdvice {
    private static final Logger logger = Logger.getLogger(QueueControllerAdvice.class);
    private final MessageSource messageSource;

    @Autowired
    public QueueControllerAdvice(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    

}