package com.fosterstory.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Entity
public class Post {
    @Id
    @GeneratedValue
    Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<PhotoPost> photoPosts = new ArrayList<>();

    @ElementCollection
    @Column(length = 20000)
    private List<String> content;

    public Post() {}

    public Post(List<PhotoPost> photoPosts, User user) {
        this.photoPosts = photoPosts;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<PhotoPost> getPhotoPosts() {
        return photoPosts;
    }

    public void setPhotoPosts(List<PhotoPost> photoPosts) {
        this.photoPosts = photoPosts;
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

}
