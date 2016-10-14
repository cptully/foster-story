package com.fosterstory.service;

import com.fosterstory.entity.TumblrPhoto;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.*;
import com.tumblr.jumblr.types.Photo;
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

        // process the retrun for our purposes
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
        }
    }
}