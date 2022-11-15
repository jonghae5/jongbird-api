package com.jonghae5.jongbirdapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonghae5.jongbirdapi.domain.*;
import com.jonghae5.jongbirdapi.service.LikeService;
import com.jonghae5.jongbirdapi.service.UserService;
import com.jonghae5.jongbirdapi.view.like.LikeResponse;
import com.jonghae5.jongbirdapi.view.result.ResultService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(LikeController.class)
@Import(SecurityConfig.class)
@MockBean(JpaMetamodelMappingContext.class)
class LikeControllerTest {

    private static User user;
    private static Comment comment;
    private static Post userPost;
    private static Image image1;
    private static Image image2;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LikeService likeService;

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
    @DisplayName("[API][PATCH][likePost] 게시금 좋아요 등록")
    void likePost() throws Exception {

        LikeResponse likeResponse = LikeResponse.builder()
                .postId(userPost.getPostId())
                .userId(user.getUserId())
                .build();
        given(likeService.addLike(any(), any())).willReturn(likeResponse);
        mockMvc.perform(patch("/post/{postId}/like", userPost.getPostId()).contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.PostId").exists())
                .andExpect(jsonPath("$.data.UserId").exists())
                .andDo(print());

        verify(likeService).addLike(any(),any());
    }

    @Test
    @DisplayName("[API][DELETE][unlikePost] 게시금 좋아요 취소")
    void unlikePost() throws Exception {

        LikeResponse likeResponse = LikeResponse.builder()
                .postId(userPost.getPostId())
                .userId(user.getUserId())
                .build();

        given(likeService.deleteLike(any(), any())).willReturn(likeResponse);
        mockMvc.perform(delete("/post/{postId}/like", userPost.getPostId()).contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.PostId").exists())
                .andExpect(jsonPath("$.data.UserId").exists())
                .andDo(print());

        verify(likeService).deleteLike(any(),any());
    }
}