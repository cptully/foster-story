package com.fosterstory.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by chris on 10/3/16.
 */
@Entity
public class Phone {
    @Id
    @GeneratedValue
    private Integer id;

    private String phoneNumber;

    private PhoneType phoneType = new PhoneType();

    public Phone() {}

    public Phone(String phoneNumber, PhoneType phoneType) {
        this.phoneNumber = phoneNumber;
        this.phoneType = phoneType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PhoneType getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(PhoneType phoneType) {
        this.phoneType = phoneType;
    }
}
