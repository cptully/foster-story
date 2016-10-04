package com.fosterstory.repository;

import com.fosterstory.entity.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by win808mac on 10/4/16.
 */
@Repository
public interface AnimalRepository extends JpaRepository<Animal, Integer>{
    Page<Animal> findAll(Pageable pageable);

/*
    @Query(value = "SELECT a FROM Animal a WHERE " +
        "(?1 = '' OR upper(a.name) LIKE upper(?1)) " +
        "AND " +
        "(?2 IS NULL OR a.breed.type.id = ?2) " +
        "AND " +
        "(?3 IS NULL OR a.breed.id = ?3) " +
        "AND " +
        "(?4 IS NULL OR a.locale = ?4) " +
        "AND " +
        "(?5 IS NULL OR a.id = ?5)")
    List<Animal> search(String name, Integer typeId, Integer breedId, Locale locale, Integer id);

*/
}
