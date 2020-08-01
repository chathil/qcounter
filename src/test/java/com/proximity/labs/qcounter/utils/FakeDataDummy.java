package com.proximity.labs.qcounter.utils;

import com.proximity.labs.qcounter.data.dto.request.NewQueueRequest;
import com.proximity.labs.qcounter.data.models.user.User;

public class FakeDataDummy {
    public static NewQueueRequest queueRequest() {
        NewQueueRequest newQueueRequest = new NewQueueRequest();
        newQueueRequest.setName("My Queue");
        newQueueRequest.setClientGeneratedId("clientGeneratedId");
        newQueueRequest.setClosedQueue(false);
        newQueueRequest.setContact("contact@gmail.com");
        newQueueRequest.setDesc("a queue desc");
        newQueueRequest.setIncrementBy(1);
        newQueueRequest.setIpAddress("192.168.1.1");
        newQueueRequest.setMax(100);
        newQueueRequest.setValidFor(32000);
        return newQueueRequest;
    }
}
