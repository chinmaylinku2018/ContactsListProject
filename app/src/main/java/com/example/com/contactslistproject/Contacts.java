package com.example.com.contactslistproject;

public class Contacts {
    private  String name;
    private String mobile;
    //create a constructor with 2 parameters

    //create

    public Contacts(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
