package com.fosterstory.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chris on 10/12/16.
 */
public class TumblrPhoto {
    private String permalink;

    //assuming that we only want the 250 pixel wide version
    private String altUrl;
    private Integer width;
    private Integer height;
    private ArrayList<String> content;

    public TumblrPhoto() {}

    public TumblrPhoto(String permalink, String altUrl, Integer width, Integer height, ArrayList<String> content) {
        this.permalink = permalink;
        this.altUrl = altUrl;
        this.width = width;
        this.height = height;
        this.content = content;
    }

    public TumblrPhoto(String permalink, String altUrl, Integer width, Integer height, String content) {
        this.permalink = permalink;
        this.altUrl = altUrl;
        this.width = width;
        this.height = height;
        setContent(content);
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getAltUrl() {
        return altUrl;
    }

    public void setAltUrl(String altUrl) {
        this.altUrl = altUrl;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(ArrayList<String> content) {
        this.content = content;
    }

    public void setContent(String content) {
        ArrayList<String> lines = (ArrayList<String>)Arrays.asList(content.split("<p>"));
        for (int i = 0; i < content.length(); i++) {
            this.content.add(lines.get(i).replace("</p", ""));
        }
    }
}
