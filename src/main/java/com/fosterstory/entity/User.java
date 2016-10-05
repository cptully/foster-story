package com.fosterstory.entity;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import java.net.URL;
import java.util.List;

/**
 * Created by chris on 10/3/16.
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Integer id;

    @Email
    private String email;

    private String nickName;

    private String firstName;
    private String middleName;
    private String lastName;


    @org.hibernate.validator.constraints.URL
    @Column(length = 2048)
    private String tumblr;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Phone> phone;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Address address;

    @Column(length = 1000)
    private String bio;

    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "animal_id")
    private List<Animal> animals;

    @OneToOne
    private Image image;

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return nickName;
    }

    public void setUsername(String username) {
        this.nickName = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Phone> getPhone() {
        return phone;
    }

    public void setPhone(List<Phone> phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getTumblr() {
        return tumblr;
    }

    public void setTumblr(String tumblr) {
        this.tumblr = tumblr;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
