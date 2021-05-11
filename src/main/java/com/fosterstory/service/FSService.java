package com.fosterstory.service;

import com.fosterstory.bean.Login;
import com.fosterstory.bean.Search;
import com.fosterstory.entity.*;
import com.fosterstory.repository.*;
import com.fosterstory.utility.PasswordStorage;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by chris on 10/3/16.
 */
@Service
public class FSService {
    final AddressRepository addressRepository;

    final UserRepository userRepository;

    final TypeRepository typeRepository;

    final BreedRepository breedRepository;

    final AnimalRepository animalRepository;

    final RoleRepository roleRepository;

    @NotNull
    public FSService(AddressRepository addressRepository, UserRepository userRepository, TypeRepository typeRepository,
                     BreedRepository breedRepository, AnimalRepository animalRepository, RoleRepository roleRepository)
    {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.typeRepository = typeRepository;
        this.breedRepository = breedRepository;
        this.animalRepository = animalRepository;
        this.roleRepository = roleRepository;
    }

    public List<Type> listTypes() {return typeRepository.findAll();}

    public List<Breed> listBreeds() {return breedRepository.findAll();}

    public Page<Animal> listAnimals(Pageable pageable) {return animalRepository.findAll(pageable);}

    public Page<Animal> listAnimals(Search search, Pageable pageable) {
        return animalRepository.search(
            search.getNameForSearch(),
            search.getTypeId(),
            search.getBreedId(),
            search.getId(),
            pageable
        );
    }


    public Animal getAnimal(Integer animalId){
        return animalRepository.getOne(animalId);
    }

    public void saveAnimal(Animal animal) {
        animalRepository.save(animal);
    }

    public User getUser(Integer id) {
        if(id != null)
        {
            Optional<User> optionalUser = userRepository.findById(id);
            return optionalUser.orElseGet(User::new);
        }
        else
        {
            return new User();
        }
    }

    public User getUserOrNull(Integer id){
        if(id != null) {
            Optional<User> optionalUser = userRepository.findById(id);
            return optionalUser.orElse(null);
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
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return userRepository.findAll(Example.of(user));
        }
        else
        {
            return Collections.emptyList();
        }
    }

    public User saveUser(User user) throws PasswordStorage.CannotPerformOperationException {

        if(user.getId() != null){
            Optional<User> optionalUser = userRepository.findById(user.getId());
            if (optionalUser.isPresent()) {
                User oldUser = optionalUser.get();
                if (!oldUser.getPassword().equals(user.getPassword())) {
                    user.setPassword(PasswordStorage.createHash(user.getPassword()));
                }
            }
            else
            {
                throw new PasswordStorage.CannotPerformOperationException("User not found");
            }
        } else {
            user.setPassword(PasswordStorage.createHash(user.getPassword()));
        }
        Role role = roleRepository.findByNameEquals("user");
        user.setRole(role);
        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public Breed getBreedById(Integer breedId) {
        return breedRepository.getOne(breedId);
    }

}
