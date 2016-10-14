package com.fosterstory.service;

import com.fosterstory.entity.TumblrPhoto;
import com.fosterstory.repository.TumbrPhotoRepository;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.Photo;
import com.tumblr.jumblr.types.PhotoPost;
import com.tumblr.jumblr.types.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by win808mac on 10/12/16.
 */
@Service
public class TumblrService {

    @Autowired
    TumbrPhotoRepository tumbrPhotoRepository;

    public void getPosts(String blogUrl) {
        // get Tumblr keys from environment
        String consumer_key = System.getenv("consumer_key");
        String consumer_secret = System.getenv("consumer_secret");

        // Create a new client
        JumblrClient client = new JumblrClient(consumer_key, consumer_secret);

        // retrieve the blog
        String blogName = blogUrl + ".tumblr.com";
        Blog blog = client.blogInfo(blogName);

        // set parameters for requesting photo posts
        Map<String, Object> params = new HashMap<>();
        params.put("type", "photo");
        params.put("tag", "animal rescue");
        params.put("limit", 5);
        params.put("filter", "html");

        // request the blog posts
        List<Post> posts = client.blogPosts(blogName, params);

        // process the return for our purposes
        List<TumblrPhoto> tumblrPhotos = new ArrayList<>();
        TumblrPhoto tumblrPhoto = new TumblrPhoto();
        List<Photo> photos;

        // iterate over posts archiving photo links
        for (Post post : posts) {
            tumblrPhoto.setContent(((PhotoPost) post).getCaption());
            photos = ((PhotoPost) post).getPhotos();
            for (Photo photo : photos) {
                tumblrPhoto.setPermalink(photos.get(0).getSizes().get(0).getUrl());
                int photoCount = photo.getSizes().size();
                for (int i = 0; i < photoCount; i++) {
                    tumblrPhoto.getPhotos().add(new com.fosterstory.entity.Photo(
                            photo.getSizes().get(i).getUrl(),
                            photo.getSizes().get(i).getHeight(),
                            photo.getSizes().get(i).getWidth()
                    ));
                }
            }
            tumblrPhotos.add(tumblrPhoto);
        }
        tumbrPhotoRepository.save(tumblrPhotos);
    }

    public List<TumblrPhoto> getTumblrPhotosByUser(Integer userId) {
        return tumbrPhotoRepository.findByUserId(userId);
    }

    public List<String> getTumblrPhotoComments(Integer tumblrPhotoId) {
        TumblrPhoto tumblrPhoto = tumbrPhotoRepository.getOne(tumblrPhotoId);
        return tumblrPhoto.getContent();
    }

    public String getTumblrPhotoAltUrl250(Integer tumblrPhotoId) {
        TumblrPhoto tumblrPhoto = tumbrPhotoRepository.getOne(tumblrPhotoId);
        List<com.fosterstory.entity.Photo> photos = tumblrPhoto.getPhotos();
        for (com.fosterstory.entity.Photo photo : photos) {
            if (photo.getWidth() == 250) {return photo.getAltUrl();}
        }
        return "";
    }
}