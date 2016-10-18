package com.fosterstory.service;

import com.fosterstory.entity.Image;
import com.fosterstory.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chris on 10/16/16.
 */
@Service
public class ImageService {
    @Autowired
    ImageRepository imageRepository;


    public Image findOne(Integer id) {
        return imageRepository.findOne(id);
    }

    public List<Image> findAll() {
        return imageRepository.findAll();
    }
}
