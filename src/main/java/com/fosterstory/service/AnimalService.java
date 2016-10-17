package com.fosterstory.service;

import com.fosterstory.entity.Animal;
import com.fosterstory.entity.Image;
import com.fosterstory.entity.Post;
import com.fosterstory.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by win808mac on 10/14/16.
 */
@Service
public class AnimalService {

    @Autowired
    AnimalRepository animalRepository;

    public List<Post> getPostsByAnimalId(Integer animalId) {
        Animal animal = animalRepository.getOne(animalId);
        return animal.getPosts();
    }


    public Animal findById(Integer id) {
        return animalRepository.findById(id);
    }
}
