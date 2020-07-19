package com.proximity.labs.qcounter.data.models.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.proximity.labs.qcounter.data.models.audit.DateAudit;

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
    this.accountType = user.getAccountType();
    this.setIsActive(user.getIsActive());
  }

  public User() {
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

  @Column(name = "account_type", nullable = false)
  private AccountType accountType = AccountType.SIGNED;

  @Column(name = "profile_completion", nullable = false)
  private int profileCompletion = 10;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive = true;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<UserDevice> userDevice;

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

  public AccountType getAccountType() {
    return accountType;
  }

  public void setAccountType(AccountType accountType) {
    this.accountType = accountType;
  }

  public int getProfileCompletion() {
    return profileCompletion;
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

  @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> auth = new ArrayList<SimpleGrantedAuthority>();
        auth.add(new SimpleGrantedAuthority(getAccountType().name()));
        return auth;
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
