package com.proximity.labs.qcounter.data.models.user;

public enum AccountType {
    GUEST ("guest"),
    SIGNED ("signed"),
    ADMIN ("admin");
  
    public final String label;
   
    private AccountType(String label) {
        this.label = label;
    }
  }