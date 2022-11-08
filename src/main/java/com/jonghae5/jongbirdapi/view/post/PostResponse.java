package com.jonghae5.jongbirdapi.view.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jonghae5.jongbirdapi.domain.Comment;
import com.jonghae5.jongbirdapi.domain.Image;
import com.jonghae5.jongbirdapi.domain.Post;
import com.jonghae5.jongbirdapi.domain.User;
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
    private Long userId;
    @JsonProperty(value = "User")
    private UserOnlyNickname user;
    @JsonProperty(value = "Comments")
    private List<Comment> comments;
//    private Long RetweetId;
    @JsonProperty(value = "Images")
    private List<Image> images = new ArrayList<>();
//    private User retweet;
    @JsonProperty(value = "Likers")
    private List<LikerOnlyId> likers = new ArrayList<>();


    static class UserForm {
        private Long id;
        private String nickname;
    }

    public PostResponse(Post post) {
        this.id = post.getPostId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();

        // 필요 없을 거 같음
        this.userId = post.getUser().getUserId();
        this.user = new UserOnlyNickname(post.getUser().getUserId(),post.getUser().getNickname());
        this.comments = post.getComments();
        this.images = post.getImages();
//        this.likers = post.getLikers()
//                .stream()
//                .map(x -> new LikerOnlyId(x.getLikeId())).collect(Collectors.toList());
        //        retweetId = retweetId;

        //TODO
        // 이미지
        // 리트윗 포스트
//        this.retweet = retweet;
    }

    @Data
    static class UserOnlyNickname {
        public UserOnlyNickname(Long id, String nickname) {
            this.id = id;
            this.nickname = nickname;
        }
        private Long id;
        private String nickname;
    }

    @Data
    static class LikerOnlyId {
        private Long id;
        public LikerOnlyId(Long id) {
            this.id = id;
        }
    }
}
