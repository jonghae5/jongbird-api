package com.jonghae5.jongbirdapi.controller;


import com.jonghae5.jongbirdapi.web.argumentResolver.Login;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.service.LikeService;
import com.jonghae5.jongbirdapi.view.like.LikeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LikeController {
    
    private final LikeService likeService;

    @PatchMapping("/post/{postId}/like")
    public LikeResponse likePost(@Login User loginUser, @PathVariable Long postId) {

        return likeService.addLike(loginUser, postId);
    }


    @DeleteMapping("/post/{postId}/like")
    public LikeResponse unlikePost(@Login User loginUser, @PathVariable Long postId) {

        return likeService.deleteLike(loginUser, postId);
    }
}
