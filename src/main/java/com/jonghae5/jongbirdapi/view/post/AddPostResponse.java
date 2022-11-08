package com.jonghae5.jongbirdapi.view.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jonghae5.jongbirdapi.domain.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Data
public class AddPostResponse {

    private Long id;
    private String content;
    @JsonProperty(value = "Images")
    private List<Image> images = new ArrayList<>();
    @JsonProperty(value = "Comments")
    private List<CommentOnlyUserNickname> comments = new ArrayList<>();
    @JsonProperty(value = "User")
    private UserOnlyNickname user;
    @JsonProperty(value = "Likers")
    private List<LikeOnlyId> likers = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonProperty(value = "RetweetId")
    private Long retweedId;
    @JsonProperty(value = "UserId")
    private Long userId;

    public void create(Post post) {
        this.id = post.getPostId();
        this.content = post.getContent();

        this.images = post.getImages();
        this.comments = post.getComments().stream().map(CommentOnlyUserNickname::new).collect(Collectors.toList());
        this.user = new UserOnlyNickname(post.getUser());

        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();


    }


    @Data
    static class UserOnlyNickname {
        private Long id;
        private String nickname;

        public UserOnlyNickname(User user) {
            this.id = user.getUserId();
            this.nickname = user.getNickname();
        }
    }

    @Data
    static class CommentOnlyUserNickname {
        private Long commentId;

        private String content;

        private UserOnlyNickname user;

        private Post post;

        public CommentOnlyUserNickname(Comment comment) {
            this.commentId = comment.getCommentId();
            this.content = comment.getContent();
            this.user = new UserOnlyNickname(comment.getUser());
            this.post = comment.getPost();
        }
    }

    @Data
    static class LikeOnlyId {
        private Long id;

        public LikeOnlyId(Like like) {
            this.id = id;
        }
    }
}
