package com.proximity.labs.qcounter.data.models.user;

import javax.annotation.Generated;
import javax.persistence.*;

import com.proximity.labs.qcounter.data.models.token.RefreshToken;

@Entity
@Table(name = "user_devices")
public class UserDevice {

    public UserDevice() {}

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_device_seq")
    @SequenceGenerator(name = "user_device_seq", allocationSize = 1, initialValue = 20)
    private Long id;

    @Column(nullable = false, unique = true)
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