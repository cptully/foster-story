package com.fosterstory.repository;

import com.fosterstory.entity.PhoneType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by chris on 10/3/16.
 */
@Repository
public interface PhoneTypeRepository extends JpaRepository<PhoneType, Integer>{
}
