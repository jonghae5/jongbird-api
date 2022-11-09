package com.jonghae5.jongbirdapi.view.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jonghae5.jongbirdapi.domain.Comment;
import com.jonghae5.jongbirdapi.domain.Image;
import com.jonghae5.jongbirdapi.domain.Like;
import com.jonghae5.jongbirdapi.domain.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RetweetPostWithUserAndImage {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonProperty(value = "User")
    private UserOnlyNickname user;
    @JsonProperty(value = "Images")
    private List<Image> images = new ArrayList<>();


    public RetweetPostWithUserAndImage(Post post) {

        this.id = post.getPostId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.user = new UserOnlyNickname(post.getUser());
        this.images = post.getImages();


    }
}
