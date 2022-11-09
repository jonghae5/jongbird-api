package com.jonghae5.jongbirdapi.controller;

import com.jonghae5.jongbirdapi.web.argumentResolver.Login;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.service.RetweetService;
import com.jonghae5.jongbirdapi.view.retweet.AddRetweetPostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RetweetController {

    private final RetweetService retweetService;
    // POST /post/1/retweet
    @PostMapping("/post/{postId}/retweet")
    public AddRetweetPostResponse addRetweet(@Login User loginUser, @PathVariable Long postId) {

        return retweetService.addRetweet(loginUser, postId);
    }
}