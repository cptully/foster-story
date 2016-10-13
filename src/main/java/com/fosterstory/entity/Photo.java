package com.fosterstory.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by win808mac on 10/13/16.
 */
@Entity
public class Photo {
    @Id
    @GeneratedValue
    private Integer id;

    private String altUrl;
    private Integer width;
    private Integer height;

    public Photo() {}

    public Photo(String altUrl, Integer width, Integer height) {
        this.altUrl = altUrl;
        this.width = width;
        this.height = height;
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
