package com.jonghae5.jongbirdapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonghae5.jongbirdapi.domain.*;
import com.jonghae5.jongbirdapi.service.CommentService;
import com.jonghae5.jongbirdapi.service.RetweetService;
import com.jonghae5.jongbirdapi.view.comment.AddCommentRequest;
import com.jonghae5.jongbirdapi.view.comment.AddCommentResponse;
import com.jonghae5.jongbirdapi.view.result.ResultService;
import com.jonghae5.jongbirdapi.view.retweet.AddRetweetPostResponse;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CommentController.class)
@Import(SecurityConfig.class)
@MockBean(JpaMetamodelMappingContext.class)
class CommentControllerTest {


    private static User user;
    private static Post userPost;
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CommentService commentService;

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

        userPost = Post.builder()
                .content("test Content")
                .user(user)
                .postId(10l)
                .build();

    }


    @Test
    @DisplayName("[API][POST][addComment] 댓글 등록")
    void addComment() throws Exception {

        AddCommentRequest addCommentRequest = new AddCommentRequest();
        addCommentRequest.setContent("New Comment");


        AddCommentResponse addCommentResponse = AddCommentResponse.builder().build();
        Comment comment = Comment.builder()
                .commentId(123L)
                .content("New Content")
                .post(userPost)
                .user(user)
                .build();
        addCommentResponse.create(comment , user);

        given(commentService.addComment(any(),any(),any())).willReturn(addCommentResponse);

        mockMvc.perform(post("/post/{postId}/comment", userPost.getPostId()).contentType(MediaType.APPLICATION_JSON)
                        .session(session)
                        .content(objectMapper.writeValueAsString(addCommentRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.content").exists())
                .andExpect(jsonPath("$.data.PostId").exists())
                .andExpect(jsonPath("$.data.UserId").exists())
                .andExpect(jsonPath("$.data.User").exists())
                .andDo(print());

        verify(commentService).addComment(any(), any(), any());


    }
}