package com.jonghae5.jongbirdapi.controller;

import com.jonghae5.jongbirdapi.view.result.ResponseService;
import com.jonghae5.jongbirdapi.view.result.SingleResult;
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
    private final ResponseService responseService;
    @PostMapping("/post/{postId}/comment")
    public SingleResult<AddCommentResponse> addComment(@Login User loginUser,
                                                       @RequestBody AddCommentRequest addCommentRequest,
                                                       @PathVariable Long postId) {

        return responseService.getSingleResult(commentService.addComment(addCommentRequest, loginUser, postId));
    }
}


//좋아요 에러