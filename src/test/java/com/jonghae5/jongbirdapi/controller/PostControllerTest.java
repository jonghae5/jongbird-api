package com.jonghae5.jongbirdapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonghae5.jongbirdapi.domain.Comment;
import com.jonghae5.jongbirdapi.domain.Image;
import com.jonghae5.jongbirdapi.domain.Post;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.service.HashtagService;
import com.jonghae5.jongbirdapi.service.PostService;
import com.jonghae5.jongbirdapi.view.post.*;
import com.jonghae5.jongbirdapi.view.result.ResultService;
import com.jonghae5.jongbirdapi.web.file.ImageFile;
import com.jonghae5.jongbirdapi.web.security.SecurityConfig;
import com.jonghae5.jongbirdapi.web.session.SessionConst;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PostController.class)
@Import(SecurityConfig.class)
@MockBean(JpaMetamodelMappingContext.class)
class PostControllerTest {


    private static User user;
    private static Comment comment;
    private static Post userPost;
    private static Image image1;
    private static Image image2;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    ResultService resultService;

    private MockHttpSession session;
    @BeforeEach
    void setupBeforeEach() {


        user = User.builder()
                .userId(1L)
                .email("testEmail@naver.com")
                .password("testPassword@!@#123")
                .nickname("testNickname")
                .build();

        session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_USER, user);

        image1 = Image.builder()
                .imageId(1L)
                .src("/test/image1.jpeg")
                .build();

        image2 = Image.builder()
                .imageId(2L)
                .src("/test/image2.jpeg")
                .build();

        comment = Comment.builder()
                .commentId(11L)
                .content("test Comment")
                .build();

        userPost = Post.builder()
                .content("test Content")
                .user(user)
                .images(List.of(image1,image2))
                .postId(10l)
                .build();

        comment.addUserAndPost(user, userPost);
    }

    @Test
    @DisplayName("[API][POST][addImage] 새로운 이미지 등록")
    void addImage() throws Exception {

        FileInputStream fileInputStream = new FileInputStream(new File("src/test/resources/image1.png"));
        MockMultipartFile multipartFile = new MockMultipartFile("image", "image1.jpeg", MediaType.IMAGE_JPEG_VALUE, fileInputStream);


        ImageFile imageFile = ImageFile.builder()
                .storeFilePath(multipartFile.getOriginalFilename())
                .uploadFilePath(new StringBuffer(multipartFile.getOriginalFilename()).reverse().toString())
                .build();

        given(postService.addImage(multipartFile)).willReturn(imageFile);


        mockMvc.perform(multipart("/post/upload/images")
                        .file(multipartFile)
                        .session(session)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").exists())
                .andDo(print());

        verify(postService).addImage(any());
    }

    @Test
    @DisplayName("[API][POST][addPost] 새로운 게시글 등록")
    void addPost() throws Exception {

        AddPostRequest addPostRequest = AddPostRequest.builder()
                .content("new Add Post")
                .build();
        Post newPost = addPostRequest.toEntity(user);

        AddPostResponse addPostResponse = new AddPostResponse();
        addPostResponse.create(newPost);
        given(postService.addPost(any(),any())).willReturn(addPostResponse);


        mockMvc.perform(post("/post").contentType(MediaType.APPLICATION_JSON)
                        .session(session)
                        .content(objectMapper.writeValueAsString(addPostRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.Images").exists())
                .andExpect(jsonPath("$.data.Comments").exists())
                .andExpect(jsonPath("$.data.User").exists())
                .andExpect(jsonPath("$.data.Likers").exists())
                .andExpect(jsonPath("$.data.content").exists())
                .andDo(print());

        verify(postService).addPost(any(), any());
    }

    @Test
    // postId
    @DisplayName("[API][PATCH][updatePost] 게시글 업데이트")
    void updatePost() throws Exception {

        UpdatePostRequest updatePostRequest = new UpdatePostRequest();
        updatePostRequest.setContent("Update Content");

        UpdatePostResponse updatePostResponse = UpdatePostResponse.builder()
                .postId(userPost.getPostId())
                .content(updatePostRequest.getContent())
                        .build();

        given(postService.updatePost(any(),any())).willReturn(updatePostResponse);

        mockMvc.perform(patch("/post/{postId}", userPost.getPostId()).contentType(MediaType.APPLICATION_JSON)
                        .session(session)
                        .content(objectMapper.writeValueAsString(updatePostRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.PostId").exists())
                .andExpect(jsonPath("$.data.content").exists())
                .andDo(print());

        verify(postService).updatePost(any(), any());
    }

    @Test
    // postId
    @DisplayName("[API][DELETE][deletePost] 게시글 삭제")
    void deletePost() throws Exception {

        DeletePostResponse deletePostResponse = new DeletePostResponse(userPost.getPostId());

        given(postService.deletePost(any(),any())).willReturn(deletePostResponse);

        mockMvc.perform(delete("/post/{postId}", userPost.getPostId()).contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.PostId").exists())
                .andDo(print());

        verify(postService).deletePost(any(), any());
    }
}