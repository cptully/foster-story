package com.fosterstory.service;

import com.fosterstory.entity.Animal;
import com.fosterstory.entity.Breed;
import com.fosterstory.entity.Type;
import com.fosterstory.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chris on 10/3/16.
 */
@Service
public class FSService {
    @Autowired
    AddressRepository addressRepository;

    @Autowired
    PhoneRepository phoneRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TypeRepository typeRepository;

    @Autowired
    BreedRepository breedRepository;

    @Autowired
    AnimalRepository animalRepository;

    public List<Type> listTypes() {return typeRepository.findAll();}

    public List<Breed> listBreeds() {return breedRepository.findAll();}

    public Page<Animal> listAnimals(Pageable pageable) {return animalRepository.findAll(pageable);}

/*
    public List<Animal> listAnimals(Search search) {return animalRepository.search(search.getName(),
            search.getTypeId(),
            search.getBreedId(),
            search.getLocale(),
            search.getId());}
*/
}
