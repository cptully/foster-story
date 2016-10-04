package com.fosterstory.repository;

import com.fosterstory.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by win808mac on 10/4/16.
 */
@Repository
public interface TypeRepository extends JpaRepository<Type, Integer>{
}
