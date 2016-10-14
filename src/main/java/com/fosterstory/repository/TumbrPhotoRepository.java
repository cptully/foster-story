package com.fosterstory.repository;

import com.fosterstory.entity.TumblrPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TumbrPhotoRepository extends JpaRepository<TumblrPhoto, Integer>{
    List<TumblrPhoto> findByUserId(Integer id);

}
