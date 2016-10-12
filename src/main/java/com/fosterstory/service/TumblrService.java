package com.fosterstory.service;

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

    public List<String> getPosts() {
        // Create a new client
        JumblrClient client = new JumblrClient(
                "tGC3qN6SxD6IK7T1FVfASEZMfraZGHSG0eqMfHTF6on3JwcBXH",
                "U870eOpOJEviApF6Ek5Cr6HommQe1ORPpMJvMiLgfpApJkeu0y"
        );

        String blogName = "suncities4paws.tumblr.com";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", "photo");
        params.put("tag", "animal rescue");
        params.put("limit", 5);
        params.put("filter", "text");
        Blog blog = client.blogInfo(blogName);
        List<Post> posts = client.blogPosts(blogName, params);
        List<String> captions = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        List<Photo> photos;

        for (Post post : posts) {
            captions.add(((PhotoPost) post).getCaption());
            photos = ((PhotoPost) post).getPhotos();
            urls.add(photos.get(0).getSizes().get(3).getUrl());
        }

        return captions;
    }
}