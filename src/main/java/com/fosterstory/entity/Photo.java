package com.fosterstory.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * Created by win808mac on 10/13/16.
 */
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Photo {
    @Id
    @GeneratedValue
    private Integer id;

    private String altUrl;
    private Integer width;
    private Integer height;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "photopost_id")
    @JsonIgnore
    private PhotoPost photoPost = new PhotoPost();

    public Photo() {}

    public Photo(String altUrl, Integer width, Integer height) {
        this.altUrl = altUrl;
        this.width = width;
        this.height = height;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAltUrl() {
        return altUrl;
    }

    public void setAltUrl(String altUrl) {
        this.altUrl = altUrl;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
