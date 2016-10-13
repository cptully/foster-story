package com.fosterstory.repository;

import com.fosterstory.entity.TumblrPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by win808mac on 10/13/16.
 */
@Repository
public interface TumbrPhotoRepository extends JpaRepository<TumblrPhoto, Integer>{
}
