package com.fosterstory.repository;

import com.fosterstory.entity.PhotoPost;
import com.fosterstory.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by win808mac on 10/19/16.
 */
@Repository
public interface PhotoPostRepository extends JpaRepository<PhotoPost, Integer>{
    Page<PhotoPost> findByPermalinkIsNotNull(Pageable pageable);
}
