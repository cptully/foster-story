package com.fosterstory.repository;

import com.fosterstory.bean.Locale;
import com.fosterstory.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by win808mac on 10/4/16.
 */
@Repository
public interface AnimalRepository extends JpaRepository<Animal, Integer>{


    @Query(value = "SELECT a FROM Animal a WHERE " +
            "(?1 ='' OR upper(a.name) LIKE upper(?1)) " +
            "AND " +
            "(?2 IS NULL OR a.breed.type.id = ?2) " +
            "AND " +
            "(?3 IS NULL OR a.breed.id = ?3) " +
            "AND " +
            "(?4 IS NULL OR a.locale.state)")
    List<Animal> search(String name, Integer typeId, Integer breedId, Locale locale, Integer id);

}
