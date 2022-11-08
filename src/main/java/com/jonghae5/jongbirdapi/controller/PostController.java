package com.jonghae5.jongbirdapi.controller;

import com.jonghae5.jongbirdapi.argumentResolver.Login;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.service.PostService;
import com.jonghae5.jongbirdapi.view.post.AddPostRequest;
import com.jonghae5.jongbirdapi.view.post.AddPostResponse;
import com.jonghae5.jongbirdapi.view.post.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<PostResponse> getPostsPages(@Login User loginUser, @RequestParam(defaultValue = "-1") Long lastId) {
        return postService.fetchPostPagesBy(lastId, loginUser);
    }

    @PostMapping("/add")
    public AddPostResponse addPost(@RequestBody @Valid AddPostRequest addPostRequest,
                                   @Login User loginUser) {
//        @RequestPart(value="imagePaths", required=false) List<String> imagePaths,
//        @RequestPart(value="content") String content,

        return postService.addPost(addPostRequest.getImagePaths(), addPostRequest.getContent(), loginUser);
    }

    @PostMapping("/upload/images")
    public String addImages(@Login User loginUser, @RequestPart(value="image", required=false) MultipartFile image) throws IOException {

        //storeFilePath
        return postService.addImage(image);

    }

    // PATCH /post/10
    // DELETE /post/10
    // POST /post/1/retweet



}
