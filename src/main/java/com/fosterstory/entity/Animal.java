package com.fosterstory.entity;

import com.sun.istack.internal.NotNull;

import javax.jws.soap.SOAPBinding;
import javax.persistence.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Created by win808mac on 10/4/16.
 */
@Entity
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
    private String gender;
    private Date adoptionDate;

    @Column(length = 5000)
    private String story;

    @Column(length = 5000)
    private String careInfo;

    @org.hibernate.validator.constraints.URL
    @Column(length = 2048)
    private String tumblr;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private List<Image> images;

    @OneToOne
    @JoinColumn(name = "user_id")
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
}

