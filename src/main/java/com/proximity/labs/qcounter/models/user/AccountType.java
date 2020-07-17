package com.proximity.labs.qcounter.models.user;

public enum AccountType {
    GUEST ("guest"),
    SIGNED ("signed"),
    ADMIN ("admin");
  
    public final String label;
   
    private AccountType(String label) {
        this.label = label;
    }
  }