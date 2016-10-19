package com.fosterstory.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by win808mac on 10/4/16.
 */
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Animal {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private Breed breed;

    private Integer age;
    private Float weight;
    private String gender;//enum
    private Date adoptionDate;

    @Column(length = 5000)
    private String story;

    @Column(length = 5000)
    private String careInfo;

    @Column(length = 2048)
    private String tumblr;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "animal")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id")
    private List<Image> images = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_fk")
    @JsonIgnore
    private User user;


    public Animal(String name, Breed breed, Integer age, Float weight, String gender) {
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.weight = weight;
        this.gender = gender;
    }

    public Animal() {
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

    public Breed getBreed() {
        return breed;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getAdoptionDate() {
        return adoptionDate;
    }

    public void setAdoptionDate(Date adoptionDate) {
        this.adoptionDate = adoptionDate;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getCareInfo() {
        return careInfo;
    }

    public void setCareInfo(String careInfo) {
        this.careInfo = careInfo;
    }

    public String getTumblr() {
        return tumblr;
    }

    public void setTumblr(String tumblr) {
        this.tumblr = tumblr;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Animal animal = (Animal) o;

        return ((getId() != null) && (getId().equals(animal.getId())));

    }

}

