package com.fosterstory.service;

import com.fosterstory.entity.Post;
import com.fosterstory.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by chris on 10/15/16.
 */
@Service
public class PostService {
    @Autowired
    PostRepository postRepository;

}
