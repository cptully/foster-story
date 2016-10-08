package com.fosterstory.service;

import com.fosterstory.bean.Login;
import com.fosterstory.bean.Search;
import com.fosterstory.entity.*;
import com.fosterstory.repository.*;
import com.fosterstory.utility.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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
    UserRepository userRepository;

    @Autowired
    TypeRepository typeRepository;

    @Autowired
    BreedRepository breedRepository;

    @Autowired
    AnimalRepository animalRepository;

    @Autowired
    RoleRepository roleRepository;

    public List<Type> listTypes() {return typeRepository.findAll();}

    public List<Breed> listBreeds() {return breedRepository.findAll();}

    public Page<Animal> listAnimals(Pageable pageable) {return animalRepository.findAll(pageable);}

    public Page<Animal> listAnimals(Search search, Pageable pageable) {return animalRepository.search(
            search.getNameForSearch(),
            search.getTypeId(),
            search.getBreedId(),
//            search.getLocale().getCity(),
            search.getId(),
            pageable);
    }


    public Animal getAnimal(Integer animalId){
        return animalRepository.getOne(animalId);
    }

    public void saveAnimal(Animal animal) {
        animalRepository.save(animal);
    }

    public User getUser(Integer id) {
        if(id != null) {
            return userRepository.findOne(id);
        } else {
            return new User();
        }
    }

    public User getUserOrNull(Integer id){
        if(id != null) {
            return userRepository.findOne(id);
        } else {
            return null;
        }
    }

    public User authenticateUser(Login login) {

        try {
            User user = userRepository.findByEmail(login.getEmail());

            if(user != null && PasswordStorage.verifyPassword(login.getPassword(), user.getPassword())){
                return user;
            }
        } catch (PasswordStorage.CannotPerformOperationException | PasswordStorage.InvalidHashException e) {
            e.printStackTrace();
        }
        login.setLoginFailed(true);
        return null;
    }

    public void createDefaultAdminUser() {
        if(userRepository.count() == 0){
            try {
                Role admin = roleRepository.getOne(1);
                User defaultAdmin = new User("admin@admin.com", admin, PasswordStorage.createHash("password"));
                userRepository.save(defaultAdmin);
            } catch (PasswordStorage.CannotPerformOperationException e) {
                e.printStackTrace();
            }
        }
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public List<User> listUsers(Integer id) {
        User user = userRepository.findOne(id);
        return userRepository.findAll(Example.of(user));
    }

    public User saveUser(User user) throws PasswordStorage.CannotPerformOperationException {

        if(user.getId() != null){
            User oldUser = userRepository.findOne(user.getId());

            if(!oldUser.getPassword().equals(user.getPassword())){
                user.setPassword(PasswordStorage.createHash(user.getPassword()));
            }
        } else {
            user.setPassword(PasswordStorage.createHash(user.getPassword()));
        }
        Role role = roleRepository.findByNameEquals("user");
        user.setRole(role);
        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        userRepository.delete(id);
    }

    public Breed getBreedById(Integer breedId) {
        return breedRepository.getOne(breedId);
    }

}
