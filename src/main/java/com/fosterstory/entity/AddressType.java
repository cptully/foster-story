package com.fosterstory.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by chris on 10/3/16.
 */
@Entity
public class AddressType {
    @Id
    @GeneratedValue
    private Integer id;

    @NotBlank
    private String name;
}
