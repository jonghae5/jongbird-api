package com.jonghae5.jongbirdapi.view.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jonghae5.jongbirdapi.domain.Follow;
import com.jonghae5.jongbirdapi.domain.Post;
import com.jonghae5.jongbirdapi.domain.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserWithoutPasswordResponse {


    private Long id;
    private String email;
    private String nickname;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonProperty(value = "Posts")
    private List<PostOnlyId> posts = new ArrayList<>();
    @JsonProperty(value = "Followers")
    private List<Follow> followers = new ArrayList<>();
    @JsonProperty(value = "Followings")
    private List<Follow> followings = new ArrayList<>();


    public void create(User user, List<Post> posts, List<Follow> followers, List<Follow> followings) {
        this.id = user.getUserId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.posts = posts.stream().map(x -> new PostOnlyId(x.getPostId())).collect(Collectors.toList());
        this.followers = followers;
        this.followings = followings;

        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

    @Data
    static class PostOnlyId {
        private Long id;
        public PostOnlyId(Long id) {
            this.id = id;
        }
    }
}
