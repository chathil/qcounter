package com.proximity.labs.qcounter.utils;

import com.proximity.labs.qcounter.data.dto.request.JoinQueueRequest;
import com.proximity.labs.qcounter.data.dto.request.NewQueueRequest;
import com.proximity.labs.qcounter.data.dto.request.SignupRequest;
import com.proximity.labs.qcounter.data.models.queue.InQueue;
import com.proximity.labs.qcounter.data.models.queue.Queue;
import com.proximity.labs.qcounter.data.models.role.Role;
import com.proximity.labs.qcounter.data.models.token.RefreshToken;
import com.proximity.labs.qcounter.data.models.user.User;
import com.proximity.labs.qcounter.data.models.user.UserDevice;

import java.time.Instant;
import java.util.*;

public class FakeDataDummy {
    public static List<NewQueueRequest> queueRequest() {
        List<NewQueueRequest> newQueueRequests = new ArrayList<>();
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

        NewQueueRequest newQueueRequest2 = new NewQueueRequest();
        newQueueRequest2.setName("My Queue2");
        newQueueRequest2.setClientGeneratedId("clientGeneratedId2");
        newQueueRequest2.setClosedQueue(false);
        newQueueRequest2.setContact("contact2@gmail.com");
        newQueueRequest2.setDesc("a queue desc 2");
        newQueueRequest2.setIncrementBy(2);
        newQueueRequest2.setIpAddress("192.168.1.2");
        newQueueRequest2.setMax(80);
        newQueueRequest2.setValidFor(32000);

        NewQueueRequest newQueueRequest3 = new NewQueueRequest();
        newQueueRequest3.setName("My Queue3");
        newQueueRequest3.setClientGeneratedId("clientGeneratedId3");
        newQueueRequest3.setClosedQueue(false);
        newQueueRequest3.setContact("contact3@gmail.com");
        newQueueRequest3.setDesc("a queue desc 3");
        newQueueRequest3.setIncrementBy(3);
        newQueueRequest3.setIpAddress("192.168.1.3");
        newQueueRequest3.setMax(90);
        newQueueRequest3.setValidFor(320001);

        newQueueRequests.add(newQueueRequest);
        newQueueRequests.add(newQueueRequest2);
        newQueueRequests.add(newQueueRequest3);

        return newQueueRequests;
    }

    public static List<NewQueueRequest> badQueueRequest() {
        List<NewQueueRequest> newQueueRequests = new ArrayList<>();
        NewQueueRequest newQueueRequest = new NewQueueRequest();
        newQueueRequest.setName("My Queue");
        newQueueRequest.setClosedQueue(false);
        newQueueRequest.setContact("contact@gmail.com");
        newQueueRequest.setDesc("a queue desc");
        newQueueRequest.setIncrementBy(1);
        newQueueRequest.setIpAddress("192.168.1.1");
        newQueueRequest.setMax(100);
        newQueueRequest.setValidFor(32000);

        newQueueRequests.add(newQueueRequest);
        return newQueueRequests;
    }

    public static List<JoinQueueRequest> joinQueueRequests() {
        List<JoinQueueRequest> joinQueueRequests = new ArrayList<>();
        JoinQueueRequest joinQueueRequest = new JoinQueueRequest();
        joinQueueRequest.setContact("085455664444");
        joinQueueRequest.setFullName("Lakeesha");
        joinQueueRequest.setIpAdress("192.168.1.1");
        joinQueueRequest.setQueueId("VlTudOiQ1K7l5kg6xLDyA38JLdPbl_2loOfUwA20cZykYgr4_6qAlxKaGdIuFZw8TKhuyIsE4D41PjOlwUo13g4");

        JoinQueueRequest joinQueueRequest2 = new JoinQueueRequest();
        joinQueueRequest2.setContact("085455664443");
        joinQueueRequest2.setFullName("Gar");
        joinQueueRequest2.setIpAdress("192.168.1.2");
        joinQueueRequest2.setQueueId("VlTudOiQ1K7l5kg6xLDyA38JLdPbl_2loOfUwA20cZykYgr4_6qAlxKaGdIuFZw8TKhuyIsE4D41PjOlwUo13g3");

        JoinQueueRequest joinQueueRequest3 = new JoinQueueRequest();
        joinQueueRequest3.setContact("085455664s443");
        joinQueueRequest3.setFullName("Mark");
        joinQueueRequest3.setIpAdress("192.168.1.2");
        joinQueueRequest3.setQueueId("VlTudOiQ1K7l5kg6xLDyA38JLdPbl_2loOfUwA20cZykYgr4_6qAlxKaGdIuFZw8TKhuyIsE4D41PjOlwUo13g1");

        JoinQueueRequest joinQueueRequest4 = new JoinQueueRequest();
        joinQueueRequest4.setContact("085455664s443");
        joinQueueRequest4.setFullName("Mark");
        joinQueueRequest4.setIpAdress("192.168.1.2");
        joinQueueRequest4.setQueueId("VlTudOiQ1K7l5kg6xLDyA38JLdPbl_2loOfUwA20cZykYgr4_6qAlxKaGdIuFZw8TKhuyIsE4D41PjOlwUo13g");

        joinQueueRequests.add(joinQueueRequest);
        joinQueueRequests.add(joinQueueRequest2);
        joinQueueRequests.add(joinQueueRequest3);
        joinQueueRequests.add(joinQueueRequest4);
        return joinQueueRequests;
    }

    public static Queue queue(User owner) {
        return new Queue(owner, "clientGeneratedId", "testQueue", "testQueueVeryShortButConsideredLongDescription", 100, 2, Date.from(Instant.now().plusMillis(320000L)), "085306550054", false, "Indonesia");
    }

    public static List<User> user() {
        User user = new User("Abdul Chathil", "chathil98@gmail.com", "password", "192.168.1.1");
        user.setId(1L);
        User user1 = new User("Yusuf Raditya", "yusuf@gmail.com", "password", "192.168.1.2");
        user1.setId(2L);
        User user2 = new User("Ihza Ahmad", "ihza@gmail.com", "password", "192.168.1.3");
        user2.setId(3L);
        User user3 = new User("Mark My Name", "mark@gmail.com", "password", "192.168.1.4");
        user3.setId(4L);
        User user4 = new User("Shawn Sanders", "shawn@gmail.com", "password", "192.168.1.5");
        user4.setId(5L);
        return List.of(user, user1, user2, user3, user4);
    }

    public static RefreshToken refreshToken(UserDevice userDevice) {
        return new RefreshToken(1L, "e765a49f-cdab-4db0-bf7e-e8faea9f74421", userDevice, 0L, Instant.now().plusMillis(320000));
    }

    public static RefreshToken badRefreshToken(UserDevice userDevice) {
        return new RefreshToken(1L, "e765a49f-cdab-4db0-bf7e-e8faea9f74421", userDevice, 0L, Instant.now().minusMillis(320000));
    }

    public static RefreshToken refreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(Instant.now().plusMillis(3200000));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setRefreshCount(0L);
        return refreshToken;
    }

    public static List<UserDevice> userDeviceSet() {
        UserDevice userDevice1 = new UserDevice("deviceToken", user().get(0));
        userDevice1.setRefreshToken(refreshToken(userDevice1));
        userDevice1.setIsRefreshActive(true);
        return List.of(userDevice1);
    }

    public static List<UserDevice> badUserDeviceSet() {
        UserDevice userDevice1 = new UserDevice("deviceToken", user().get(0));
        userDevice1.setRefreshToken(refreshToken(userDevice1));
        userDevice1.setIsRefreshActive(true);
        userDevice1.setIsRefreshActive(false);
        return List.of(userDevice1);
    }

    public static SignupRequest signupRequest() {
        SignupRequest request = new SignupRequest();
        request.setName("Abdul Chathil");
        request.setPassword("password");
        request.setIpAddress("192.168.1.1");
        request.setEmail("chathil98@gmail.com");
        request.setDeviceToken("deviceToken");
        return request;
    }

    public static Set<InQueue> inQueues() {
        InQueue inQueue = new InQueue(queue(user().get(0)), user().get(1), user().get(1).getName(), user().get(1).getEmail(), 1);
        InQueue inQueue1 = new InQueue(queue(user().get(0)), user().get(2), user().get(2).getName(), user().get(2).getEmail(), 2);
        InQueue inQueue2 = new InQueue(queue(user().get(0)), user().get(3), user().get(3).getName(), user().get(3).getEmail(), 3);
        InQueue inQueue3 = new InQueue(queue(user().get(0)), user().get(4), user().get(4).getName(), user().get(4).getEmail(), 4);
        return Set.of(inQueue, inQueue1, inQueue2, inQueue3);
    }
}
