package com.fosterstory.repository;

import com.fosterstory.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by chris on 10/16/16.
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Integer>{

}
