package com.fosterstory.repository;

import com.fosterstory.entity.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by win808mac on 10/4/16.
 */
@Repository
public interface AnimalRepository extends JpaRepository<Animal, Integer>{
    Page<Animal> findAll(Pageable pageable);

    @Query(value = "SELECT a FROM Animal a WHERE " +
            "(?1 = '' OR upper(a.name) LIKE upper(?1)) " +
            "AND (?2 IS NULL OR a.breed.type.id = ?2) " +
            "AND (?3 IS NULL OR a.breed.id = ?3) " +
            "AND (?4 = '' OR upper(a.user.address.city) like upper(?4)) " +
            "AND (?5 IS NULL OR a.id = ?5)")
    Page<Animal> search(String name, Integer typeId, Integer breedId, String city, Integer id, Pageable pageable);

    @Query(value = "SELECT a FROM Animal a WHERE " +
            "(?1 = '' OR upper(a.name) LIKE upper(?1)) " +
            "AND (?2 IS NULL OR a.breed.type.id = ?2) " +
            "AND (?3 IS NULL OR a.breed.id = ?3) " +
            "AND (?4 IS NULL OR a.id = ?4)")
    Page<Animal> search(String name, Integer typeId, Integer breedId, Integer id, Pageable pageable);

    Optional<Animal> findById(Integer animalId);

}
