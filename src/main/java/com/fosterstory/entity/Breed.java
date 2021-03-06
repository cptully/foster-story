package com.fosterstory.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.persistence.*;
import javax.transaction.Transactional;

/**
 * Created by win808mac on 10/4/16.
 */
@Entity
@Transactional
public class Breed {
    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @NotBlank
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Type type;

    public Breed() {
    }

    public Breed(String name, Type type) {
        this.name = name;
        this.type = type;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
