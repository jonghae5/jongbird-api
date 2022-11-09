package com.jonghae5.jongbirdapi.view.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jonghae5.jongbirdapi.domain.Comment;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.view.dto.UserOnlyNickname;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AddCommentResponse {

    @JsonProperty(value = "id")
    private Long commentId;
    @JsonProperty(value = "content")
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonProperty(value = "UserId")
    private Long userId;
    @JsonProperty(value = "PostId")
    private Long postId;
    @JsonProperty(value = "User")
    private UserOnlyNickname user;


    public void create(Comment comment , User user) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.user = new UserOnlyNickname(user.getUserId(), user.getNickname());
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.userId = comment.getUser().getUserId();
        this.postId = comment.getPost().getPostId();
    }
}
