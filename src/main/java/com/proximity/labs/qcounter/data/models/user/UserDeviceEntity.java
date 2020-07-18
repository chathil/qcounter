package com.proximity.labs.qcounter.data.models.user;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_devices")
public class UserDeviceEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String deviceToken;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public UserDeviceEntity(String deviceToken, UserEntity user) {
        this.deviceToken = deviceToken;
        this.user = user;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public UserEntity getUser() {
        return user;
    }

}