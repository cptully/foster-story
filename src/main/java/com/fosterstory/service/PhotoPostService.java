package com.fosterstory.service;

import com.fosterstory.entity.PhotoPost;
import com.fosterstory.entity.Post;
import com.fosterstory.repository.PhotoPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by win808mac on 10/19/16.
 */
@Service
public class PhotoPostService {
    @Autowired
    PhotoPostRepository photoPostRepository;

    public Page<PhotoPost> listPosts(Pageable pageable) {
        return photoPostRepository.findByPermalinkIsNotNull(pageable);
    }

}
