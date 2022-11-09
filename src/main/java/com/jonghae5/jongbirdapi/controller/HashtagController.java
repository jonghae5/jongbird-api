package com.jonghae5.jongbirdapi.controller;

import com.jonghae5.jongbirdapi.web.argumentResolver.Login;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.service.HashtagService;
import com.jonghae5.jongbirdapi.view.hashtag.GetPostWithHashtagResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/hashtag")
public class HashtagController {

    private final HashtagService hashtagService;
    // GET /hashtag/리액트
    @GetMapping("/{hashtag}")
    public List<GetPostWithHashtagResponse> getPostWithHashtag(@Login User loginUser,
                                                               @PathVariable String hashtag,
                                                               @RequestParam(defaultValue = "-1") Long lastId) {

        return hashtagService.fetchPostPagesByHashtag(loginUser, hashtag, lastId);
    }
}
