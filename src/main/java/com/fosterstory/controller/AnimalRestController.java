package com.fosterstory.controller;

import com.fosterstory.entity.Animal;
import com.fosterstory.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Created by win808mac on 10/11/16.
 */

@RestController
public class AnimalRestController {
   @Autowired
    private AnimalRepository animalRepository;

    @RequestMapping(path = "/userAnimals/{animalId}")
    public Animal getAnimal(@PathVariable Integer animalId){
        return animalRepository.getOne(animalId);
    }
}
