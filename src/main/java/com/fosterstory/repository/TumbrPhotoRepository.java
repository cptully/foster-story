package com.fosterstory.repository;

import com.fosterstory.entity.PhotoPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TumbrPhotoRepository extends JpaRepository<PhotoPost, Integer>{

}
