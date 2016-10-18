package com.fosterstory.entity;

import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Id;

import javax.persistence.*;

/**
 * Created by chris on 10/3/16.
 */
@Entity
public class Image {
    @Id
    @GeneratedValue
    private Integer id;

    @Basic(fetch= FetchType.LAZY)
    private byte[] image;

    private String contentType;

    public Image(byte[] image, String contentType) {
        this.image = image;
        this.contentType = contentType;
    }

    public Image() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}