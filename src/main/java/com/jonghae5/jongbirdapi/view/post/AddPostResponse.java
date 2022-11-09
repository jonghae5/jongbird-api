package com.jonghae5.jongbirdapi.view.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jonghae5.jongbirdapi.domain.*;
import com.jonghae5.jongbirdapi.view.dto.CommentOnlyUserNickname;
import com.jonghae5.jongbirdapi.view.dto.LikeOnlyId;
import com.jonghae5.jongbirdapi.view.dto.UserOnlyNickname;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Data
public class AddPostResponse {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonProperty(value = "Images")
    private List<Image> images = new ArrayList<>();
    @JsonProperty(value = "Comments")
    private List<CommentOnlyUserNickname> comments = new ArrayList<>();
    @JsonProperty(value = "User")
    private UserOnlyNickname user;
    @JsonProperty(value = "Likers")
    private List<LikeOnlyId> likers = new ArrayList<>();


    public void create(Post post) {
        this.id = post.getPostId();
        this.content = post.getContent();

        this.images = post.getImages();
        this.comments = post.getComments().stream().map(CommentOnlyUserNickname::new).collect(Collectors.toList());
        this.user = new UserOnlyNickname(post.getUser());

        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}
