package com.fosterstory.repository;

import com.fosterstory.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by win808mac on 10/14/16.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{
}
