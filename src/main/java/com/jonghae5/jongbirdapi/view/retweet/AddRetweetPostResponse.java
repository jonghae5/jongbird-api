package com.jonghae5.jongbirdapi.view.retweet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jonghae5.jongbirdapi.domain.*;
import com.jonghae5.jongbirdapi.view.dto.CommentOnlyUserNickname;
import com.jonghae5.jongbirdapi.view.dto.LikeOnlyId;
import com.jonghae5.jongbirdapi.view.dto.RetweetPost;
import com.jonghae5.jongbirdapi.view.dto.UserOnlyNickname;
import com.jonghae5.jongbirdapi.view.post.AddPostResponse;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AddRetweetPostResponse {

    private Long id;
    private String content;

    @JsonProperty(value = "Retweet")
    private RetweetPost retweet;

    @JsonProperty(value = "User")
    private UserOnlyNickname user;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonProperty(value = "Images")
    private List<Image> images = new ArrayList<>();
    @JsonProperty(value = "Comments")
    private List<CommentOnlyUserNickname> comments = new ArrayList<>();

    @JsonProperty(value = "Likers")
    private List<LikeOnlyId> likers = new ArrayList<>();

    public void create(Post originalPost, Post retweetPost) {
        this.id = retweetPost.getPostId();
        this.content = retweetPost.getContent();

        this.user = new UserOnlyNickname(retweetPost.getUser());
        this.createdAt = retweetPost.getCreatedAt();
        this.updatedAt = retweetPost.getUpdatedAt();

        this.retweet = new RetweetPost(originalPost);

        this.images = retweetPost.getImages();
        this.comments = retweetPost.getComments().stream().map(CommentOnlyUserNickname::new).collect(Collectors.toList());
        this.likers = retweetPost.getLikers().stream().map(LikeOnlyId::new).collect(Collectors.toList());

    }


}
