package com.jonghae5.jongbirdapi.controller;


import com.jonghae5.jongbirdapi.web.argumentResolver.Login;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.service.PostService;
import com.jonghae5.jongbirdapi.view.post.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostsController {

    private final PostService postService;

    @GetMapping
    public List<PostResponse> getPostsPages(@Login User loginUser, @RequestParam(defaultValue = "-1") Long lastId) {
        return postService.fetchPostPagesBy(lastId, loginUser);
    }

    @GetMapping("/user/{userId}")
    public List<PostResponse> getPostsPagesByUser(@Login User loginUser,
                                            @PathVariable Long userId,
                                            @RequestParam(defaultValue = "-1") Long lastId) {
        return postService.fetchPostPagesByUser(lastId, loginUser, userId);
    }
}
