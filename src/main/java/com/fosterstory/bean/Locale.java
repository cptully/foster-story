package com.fosterstory.bean;

/**
 * Created by chris on 10/3/16.
 */
public class Locale {
    private String state;
    private String city;
    private String zip;

    public Locale() {}

    public Locale(String state, String city, String zip) {
        this.state = state;
        this.city = city;
        this.zip = zip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
