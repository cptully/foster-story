package com.fosterstory.service;

import com.fosterstory.entity.Image;
import com.fosterstory.repository.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by chris on 10/16/16.
 */
@Service
public class ImageService {
    final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository)
    {
        this.imageRepository = imageRepository;
    }

    public Image findOne(Integer id) {
        Optional<Image> optionalImage = imageRepository.findById(id);
        return optionalImage.orElseGet(Image::new);
    }

    public List<Image> findAll() {
        return imageRepository.findAll();
    }
}
