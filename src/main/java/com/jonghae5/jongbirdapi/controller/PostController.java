package com.jonghae5.jongbirdapi.controller;

import com.jonghae5.jongbirdapi.web.argumentResolver.Login;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.service.PostService;
import com.jonghae5.jongbirdapi.view.post.DeletePostResponse;
import com.jonghae5.jongbirdapi.view.post.UpdatePostRequest;
import com.jonghae5.jongbirdapi.view.post.UpdatePostResponse;
import com.jonghae5.jongbirdapi.view.post.AddPostRequest;
import com.jonghae5.jongbirdapi.view.post.AddPostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @PostMapping
    public AddPostResponse addPost(@RequestBody @Valid AddPostRequest addPostRequest,
                                   @Login User loginUser) {
//        @RequestPart(value="imagePaths", required=false) List<String> imagePaths,
//        @RequestPart(value="content") String content,

        return postService.addPost(addPostRequest, loginUser);
    }

    @PostMapping("/upload/images")
    public String addImages(@Login User loginUser, @RequestPart(value="image", required=false) MultipartFile image) throws IOException {

        //storeFilePath
        return postService.addImage(image);
    }

    // PATCH /posts/10
    @PatchMapping("/{postId}")
    public UpdatePostResponse updatePost(@Login User loginUser, @RequestBody @Valid UpdatePostRequest updatePostRequest, @PathVariable Long postId) {

        return postService.updatePost(updatePostRequest, loginUser);
    }
    // DELETE /post/10
    @DeleteMapping("/{postId}")
    public DeletePostResponse deletePost(@Login User loginUser, @PathVariable Long postId) {

        return postService.deletePost(loginUser, postId);
    }


}
