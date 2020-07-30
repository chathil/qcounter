package com.proximity.labs.qcounter.data.models.user;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import com.proximity.labs.qcounter.data.models.audit.DateAudit;
import com.proximity.labs.qcounter.data.models.queue.InQueue;
import com.proximity.labs.qcounter.data.models.queue.Queue;
import com.proximity.labs.qcounter.data.models.role.Role;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User extends DateAudit implements UserDetails {

    public User(String name, String email, String password, String ipAddress) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.ipAddress = ipAddress;
    }

    public User(User user) {
        this.id = user.getId();
        this.setCreatedAt(user.getCreatedAt());
        this.setUpdatedAt(user.getUpdatedAt());
        this.userDevice = user.getUserDevice();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.ipAddress = user.getIpAddress();
        this.setIsActive(user.getIsActive());
        this.roles = user.getRoles();
        this.queues = user.queues;
        this.myQueues = user.myQueues;
        this.location = user.location;
        this.profileCompletion = user.profileCompletion;
    }

    public User() {
        this.queues = new HashSet<>();
        this.myQueues = new HashSet<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true, nullable = true)
    private String email;

    @Column(nullable = true)
    private String password;

    private String location = "Indonesia";

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "profile_completion", nullable = false)
    private int profileCompletion = 10;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserDevice> userDevice;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<InQueue> queues = new HashSet<>();

    @OneToMany(mappedBy = "owner",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Queue> myQueues;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_authority", joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> roles = new HashSet<>();

    public Set<InQueue> getQueues() {
        return queues;
    }

    public Set<Queue> getMyQueues() {
        return myQueues;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return location;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getProfileCompletion() {
        return profileCompletion;
    }

    public void setQueues(List<InQueue> queues) {
        this.queues.clear();
        this.queues.addAll(queues);
    }

    public void setMyQueues(List<Queue> myQueues) {
        this.queues.clear();
        this.myQueues.addAll(myQueues);
    }

    public void setProfileCompletion(int profileCompletion) {
        this.profileCompletion = profileCompletion;
    }

    public List<UserDevice> getUserDevice() {
        return userDevice;
    }

    public void setUserDevice(List<UserDevice> userDevice) {
        this.userDevice = userDevice;
    }

    public String hexId() {
        return Long.toHexString(id);
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> authorities) {
        roles = authorities;
    }

    public void addRole(Role role) {
        roles.add(role);
        role.getUserList().add(this);
    }

    public void addRoles(Set<Role> roles) {
        roles.forEach(this::addRole);
    }

    public void removeRole(Role role) {
        roles.remove(role);
        role.getUserList().remove(this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRole().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return getIsActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User that = (User) obj;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
