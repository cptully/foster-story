package com.fosterstory.entity;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.ArrayList;
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

    private String tumblr;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address = new Address();

    @Column(length = 1000)// not a real good reason to limit this
    private String bio;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Animal> animals;

    @OneToOne
    private Image image;

    @ManyToOne
    private Role role;

    @Length(min = 8, max = 100)
    private String password;

    @Transient
    private String oldPassword;

    @Transient
    private String confirmPassword;

    private String oAuthConsumerKey;
    private String oAuthSecretKey;

    public User() {}

    public User(String email, Role role, String password) {
        this.email = email;
        this.role = role;
        this.password = password;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
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

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
