package com.jonghae5.jongbirdapi.view.dto;

import com.jonghae5.jongbirdapi.domain.Comment;
import com.jonghae5.jongbirdapi.domain.Post;
import lombok.Data;

@Data
public class CommentOnlyUserNickname {
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