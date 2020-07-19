package com.proximity.labs.qcounter.data.models.user;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.proximity.labs.qcounter.data.models.token.RefreshToken;

@Entity
@Table(name = "user_devices")
public class UserDevice {

    public UserDevice() {}

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String deviceToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(optional = false, mappedBy = "userDevice")
    private RefreshToken refreshToken;

    @Column(name = "is_refresh_active")
    private Boolean isRefreshActive;

    public UserDevice(String deviceToken, User user) {
        this.deviceToken = deviceToken;
        this.user = user;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public User getUser() {
        return user;
    }

    public Boolean getIsRefreshActive() {
        return isRefreshActive;
    }

    public void setIsRefreshActive(Boolean isRefreshActive) {
        this.isRefreshActive = isRefreshActive;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    

}