package com.fosterstory.service;

import com.fosterstory.bean.TumblrPhoto;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.*;
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

    public List<TumblrPhoto> getPosts() {
        // Create a new client
        JumblrClient client = new JumblrClient(
                "tGC3qN6SxD6IK7T1FVfASEZMfraZGHSG0eqMfHTF6on3JwcBXH",
                "U870eOpOJEviApF6Ek5Cr6HommQe1ORPpMJvMiLgfpApJkeu0y"
        );

        // retrieve the blog
        String blogName = "suncities4paws.tumblr.com";
        Blog blog = client.blogInfo(blogName);

        // set parameters for requesting photo posts
        Map<String, Object> params = new HashMap<>();
        params.put("type", "photo");
        params.put("tag", "animal rescue");
        params.put("limit", 5);
        params.put("filter", "text");

        // request the blog posts
        List<Post> posts = client.blogPosts(blogName, params);

        // process the retrun for our purposes
        List<TumblrPhoto> tumblrPhotos = new ArrayList<>();
        TumblrPhoto tumblrPhoto = new TumblrPhoto();
        List<Photo> photos;

        for (Post post : posts) {
            tumblrPhoto.setContent(((PhotoPost) post).getCaption());
            photos = ((PhotoPost) post).getPhotos();
            for (Photo photo : photos) {
                tumblrPhoto.setPermalink(photos.get(0).getSizes().get(0).getUrl());
                tumblrPhoto.setAltUrl(photos.get(0).getSizes().get(3).getUrl());
                tumblrPhoto.setHeight(photos.get(0).getSizes().get(3).getHeight());
                tumblrPhoto.setWidth(photos.get(0).getSizes().get(3).getWidth());
            }
            tumblrPhoto.setPermalink(photos.get(0).getSizes().get(0).getUrl());
            tumblrPhoto.setAltUrl(photos.get(0).getSizes().get(3).getUrl());
            tumblrPhoto.setHeight(photos.get(0).getSizes().get(3).getHeight());
            tumblrPhoto.setWidth(photos.get(0).getSizes().get(3).getWidth());
            tumblrPhotos.add(tumblrPhoto);
        }

        return tumblrPhotos;
    }
}