package com.fosterstory.repository;

import com.fosterstory.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by win808mac on 10/13/16.
 */
@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer>{
    List<Photo> findByTumblrPhotoId(Integer id);
}
