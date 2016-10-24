package com.fosterstory.service;

import com.fosterstory.repository.PostRepository;
import com.fosterstory.repository.TumbrPhotoRepository;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Photo;
import com.tumblr.jumblr.types.PhotoPost;
import com.tumblr.jumblr.types.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    // Create a new client
    private JumblrClient client;
    private LocalDateTime lastTumblrPull;
    private final long REFRESH_LIMIT = 0;

    public TumblrService() {
        // get Tumblr keys from environment
        String consumer_key = System.getenv("consumer_key");
        String consumer_secret = System.getenv("consumer_secret");

        // Create a new client
        this.client = new JumblrClient(consumer_key, consumer_secret);
        this.lastTumblrPull = LocalDateTime.now().minusMinutes(REFRESH_LIMIT);
    }

    public void getPostsFromTumblr(String blogUrl) {
        // set parameters for requesting photo posts
        Map<String, Object> options = new HashMap<>();
        options.put("type", "photo");
        options.put("filter", "html");

        List<Post> tumblrPosts;

        if (ChronoUnit.MINUTES.between(lastTumblrPull, LocalDateTime.now()) > REFRESH_LIMIT) {
            // retrieve the blog
            if (!blogUrl.equals("")) {
                String blogName = blogUrl + ".tumblr.com";

                options.put("limit", 5);
                options.put("tag", "animal rescue");

                // request the blog posts
                tumblrPosts = client.blogPosts(blogName, options);
            } else {
                tumblrPosts = client.tagged("animal rescue", options);
            }

            this.lastTumblrPull = LocalDateTime.now();


            // process the return for our purposes
            com.fosterstory.entity.Post post;
            com.fosterstory.entity.PhotoPost photoPost;
            List<Photo> tumblrPhotos;

            // iterate over posts archiving photo links
            for (Post tumblrPost : tumblrPosts) {
                post = new com.fosterstory.entity.Post();
                post.setTumblrId(tumblrPost.getId());
                post.setBlogName(tumblrPost.getBlogName());
                post.setPostUrl(tumblrPost.getPostUrl());
                if (tumblrPost.getType().equals("photo")) {
                    post.setCaption(((PhotoPost) tumblrPost).getCaption());
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
                    if (photoPost.getPermalink() != null) {
                        post.getPhotoPosts().add(photoPost);
                        photoPost.setPost(post);
                        postRepository.save(post);
                    }
                }
            }
        }
    }


    public Page<com.fosterstory.entity.Post> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

}