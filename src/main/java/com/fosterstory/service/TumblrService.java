package com.fosterstory.service;

import com.fosterstory.repository.PostRepository;
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

    @Autowired
    PostRepository postRepository;

    public void getPosts(String blogUrl) {
        // get Tumblr keys from environment
        String consumer_key = System.getenv("consumer_key");
        String consumer_secret = System.getenv("consumer_secret");

        // Create a new client
        JumblrClient client = new JumblrClient(consumer_key, consumer_secret);

        // retrieve the blog
        String blogName = blogUrl + ".tumblr.com";

        // set parameters for requesting photo posts
        Map<String, Object> params = new HashMap<>();
        params.put("type", "photo");
        params.put("tag", "animal rescue");
        params.put("limit", 5);
        params.put("filter", "html");

        // request the blog posts
        List<Post> tumblrPosts = client.blogPosts(blogName, params);

        // process the return for our purposes
        com.fosterstory.entity.Post post;
        com.fosterstory.entity.PhotoPost photoPost;
        List<Photo> tumblrPhotos;

        // iterate over posts archiving photo links
        for (Post tumblrPost : tumblrPosts) {
            post = new com.fosterstory.entity.Post();
            post.setContent(((PhotoPost) tumblrPost).getCaption());
            tumblrPhotos = ((PhotoPost) tumblrPost).getPhotos();
            photoPost = new com.fosterstory.entity.PhotoPost();

            for (Photo tumblrPhoto : tumblrPhotos) {
                photoPost.setPermalink(tumblrPhoto.getSizes().get(0).getUrl());
                int tumblrPhotoCount = tumblrPhoto.getSizes().size();
                for (int i = 0; i < tumblrPhotoCount; i++) {
                    photoPost.getPhotos().add(new com.fosterstory.entity.Photo(
                            tumblrPhoto.getSizes().get(i).getUrl(),
                            tumblrPhoto.getSizes().get(i).getHeight(),
                            tumblrPhoto.getSizes().get(i).getWidth()
                    ));
                }
            }
            post.getPhotoPosts().add(photoPost);
            postRepository.save(post);
        }
    }


}