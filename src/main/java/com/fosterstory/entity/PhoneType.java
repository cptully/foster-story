package com.fosterstory.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by chris on 10/3/16.
 */
@Entity
public class PhoneType {
    @Id
    @GeneratedValue
    private Integer id;

    @NotBlank
    private String name;

    public PhoneType() {}

    public PhoneType(String name) {
        this.name = name;
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
}
