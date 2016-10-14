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
    private List<Photo> photos = new ArrayList<>();

    @ElementCollection
    @Column(length = 20000)
    private List<String> content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

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
        List<String> lines = Arrays.asList(content.split("</p>"));
        this.content = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
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
