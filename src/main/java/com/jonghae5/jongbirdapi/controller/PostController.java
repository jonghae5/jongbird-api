package com.jonghae5.jongbirdapi.controller;

import com.jonghae5.jongbirdapi.view.result.ResponseService;
import com.jonghae5.jongbirdapi.view.result.SingleResult;
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
    private final ResponseService responseService;
    @PostMapping
    public SingleResult<AddPostResponse> addPost(@RequestBody @Valid AddPostRequest addPostRequest,
                                                @Login User loginUser) {
//        @RequestPart(value="imagePaths", required=false) List<String> imagePaths,
//        @RequestPart(value="content") String content,

        return responseService.getSingleResult(postService.addPost(addPostRequest, loginUser));
    }

    @PostMapping("/upload/images")
    public SingleResult<String> addImages(@Login User loginUser, @RequestPart(value="image", required=false) MultipartFile image) throws IOException {

        //storeFilePath
        return responseService.getSingleResult(postService.addImage(image));
    }

    // PATCH /posts/10
    @PatchMapping("/{postId}")
    public SingleResult<UpdatePostResponse> updatePost(@Login User loginUser, @RequestBody @Valid UpdatePostRequest updatePostRequest, @PathVariable Long postId) {

        return responseService.getSingleResult(postService.updatePost(updatePostRequest, loginUser));
    }
    // DELETE /post/10
    @DeleteMapping("/{postId}")
    public SingleResult<DeletePostResponse> deletePost(@Login User loginUser, @PathVariable Long postId) {

        return responseService.getSingleResult(postService.deletePost(loginUser, postId));
    }


}
