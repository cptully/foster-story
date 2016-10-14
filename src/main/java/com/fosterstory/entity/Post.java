package com.fosterstory.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by win808mac on 10/14/16.
 */
@Entity
public class Post {
    @Id
    @GeneratedValue
    Integer id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<TumblrPhoto> tumblrPhotos = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Post() {}

    public Post(List<TumblrPhoto> tumblrPhotos, User user) {
        this.tumblrPhotos = tumblrPhotos;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TumblrPhoto> getTumblrPhotos() {
        return tumblrPhotos;
    }

    public void setTumblrPhotos(List<TumblrPhoto> tumblrPhotos) {
        this.tumblrPhotos = tumblrPhotos;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
