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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Locale locale = (Locale) o;

        if (getState() != null ? !getState().equals(locale.getState()) : locale.getState() != null) return false;
        if (getCity() != null ? !getCity().equals(locale.getCity()) : locale.getCity() != null) return false;
        return getZip() != null ? getZip().equals(locale.getZip()) : locale.getZip() == null;

    }

    @Override
    public int hashCode() {
        int result = getState() != null ? getState().hashCode() : 0;
        result = 31 * result + (getCity() != null ? getCity().hashCode() : 0);
        result = 31 * result + (getZip() != null ? getZip().hashCode() : 0);
        return result;
    }
}
