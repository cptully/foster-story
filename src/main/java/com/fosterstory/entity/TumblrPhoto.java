package com.fosterstory.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class TumblrPhoto {
    @Id
    @GeneratedValue
    private Integer id;

    private String permalink;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "tumblr_photo_id")
    private List<Photo> photos;

    @ElementCollection
    @Column(length = 20000)
    private List<String> content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

    public TumblrPhoto() {}

    public TumblrPhoto(String permalink, ArrayList<String> content) {
        this.permalink = permalink;
        this.content = content;
    }

    public TumblrPhoto(String permalink, String content) {
        this.permalink = permalink;
        setContent(content);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public void setContent(String content) {
        ArrayList<String> lines = (ArrayList<String>)Arrays.asList(content.split("</p>"));
        for (int i = 0; i < content.length(); i++) {
            this.content.add(lines.get(i).replace("<p>", ""));
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }
}
