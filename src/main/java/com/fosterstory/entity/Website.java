package com.fosterstory.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.net.URL;

/**
 * Created by chris on 10/3/16.
 */
@Entity
public class Website {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;

    @org.hibernate.validator.constraints.URL
    private java.net.URL url;

    private String oauth;
    private User user;

    public Website() {
    }

    public Website(String name, URL url, User user) {
        this.name = name;
        this.url = url;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getOauth() {
        return oauth;
    }

    public void setOauth(String oauth) {
        this.oauth = oauth;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
