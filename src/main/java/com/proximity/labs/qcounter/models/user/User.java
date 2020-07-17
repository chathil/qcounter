package com.proximity.labs.qcounter.models.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
public class User {

  public User(String name, String email, String password, String ipAddress) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.ipAddress = ipAddress;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Integer id;

  private final String name;

  @Column(nullable = true)
  private final String email;

  @Column(nullable = true)
  private final String password;

  private final String location = "Indonesia";

  @Column(name = "ip_address", nullable = false)
  private final String ipAddress;

  @Column(name = "profile_picture", nullable = true)
  private String profilePicture;

  @Column(name = "account_type", nullable = false)
  private AccountType accountType = AccountType.GUEST;

  @Column(name = "profile_completion", nullable = false)
  private int profileCompletion = 10;

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getLocation() {
    return location;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public String getProfilePicture() {
    return profilePicture;
  }

  public void setProfilePicture(String profilePicture) {
    this.profilePicture = profilePicture;
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
}

