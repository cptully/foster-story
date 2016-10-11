package com.fosterstory.controller;

import com.fosterstory.entity.Breed;
import com.fosterstory.repository.BreedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by win808mac on 10/11/16.
 */
@RestController
public class BreedsRestController {

    @Autowired
    private BreedRepository breedRepository;

    @RequestMapping(path = "/breeds/{typeId}")
    public List<Breed> listBreeds(@PathVariable Integer typeId) {
        return breedRepository.findByTypeId(typeId);
    }
}
