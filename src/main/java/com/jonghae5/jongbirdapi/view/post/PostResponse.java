package com.jonghae5.jongbirdapi.view.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jonghae5.jongbirdapi.domain.Comment;
import com.jonghae5.jongbirdapi.domain.Image;
import com.jonghae5.jongbirdapi.domain.Post;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.view.dto.LikeOnlyId;
import com.jonghae5.jongbirdapi.view.dto.RetweetPostWithUserAndImage;
import com.jonghae5.jongbirdapi.view.dto.UserOnlyNickname;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostResponse {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonProperty(value = "User")
    private UserOnlyNickname user;
    @JsonProperty(value = "Comments")
    private List<Comment> comments;

    @JsonProperty(value = "Images")
    private List<Image> images = new ArrayList<>();

    @JsonProperty(value = "Likers")
    private List<LikeOnlyId> likers = new ArrayList<>();

    @JsonProperty(value = "Retweet")
    private RetweetPostWithUserAndImage retweet;


    public PostResponse(Post post) {
        this.id = post.getPostId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();

        this.user = new UserOnlyNickname(post.getUser());
        this.comments = post.getComments();
        this.images = post.getImages();
        this.likers = post.getLikers()
                .stream()
                .map(x -> new LikeOnlyId(x)).collect(Collectors.toList());

        if (post.getRetweet()!=null) {
            this.retweet = new RetweetPostWithUserAndImage(post.getRetweet().getPost());
        }
    }




}
