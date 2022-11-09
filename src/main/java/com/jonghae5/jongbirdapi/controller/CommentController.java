package com.jonghae5.jongbirdapi.controller;

import com.jonghae5.jongbirdapi.web.argumentResolver.Login;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.service.CommentService;
import com.jonghae5.jongbirdapi.view.comment.AddCommentRequest;
import com.jonghae5.jongbirdapi.view.comment.AddCommentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post/{postId}/comment")
    public AddCommentResponse addComment(@Login User loginUser,
                                         @RequestBody AddCommentRequest addCommentRequest,
                                         @PathVariable Long postId) {

        return commentService.addComment(addCommentRequest, loginUser, postId);
    }
}


//좋아요 에러