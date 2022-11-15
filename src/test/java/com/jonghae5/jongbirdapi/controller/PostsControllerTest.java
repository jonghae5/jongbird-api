package com.jonghae5.jongbirdapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonghae5.jongbirdapi.domain.*;
import com.jonghae5.jongbirdapi.service.PostService;
import com.jonghae5.jongbirdapi.service.UserService;
import com.jonghae5.jongbirdapi.view.post.PostResponse;
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
import org.springframework.security.core.parameters.P;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(PostsController.class)
@Import(SecurityConfig.class)
@MockBean(JpaMetamodelMappingContext.class)
class PostsControllerTest {


    private static User user;
    private static User otherUser;

    private static Comment comment;
    private static Post userPost;
    private static Post otherPost;

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


        otherUser = User.builder()
                .userId(191l)
                .email("follower@naver.com")
                .password("follower@!@#123")
                .nickname("follower")
                .build();


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

        otherPost = Post.builder()
                .content("test Content2")
                .user(otherUser)
                .images(List.of(image1))
                .postId(11l)
                .build();

        comment.addUserAndPost(user, userPost);
    }

    @Test
    @DisplayName("[API][POST][getPostsPagesByUser] Posts 가져오기")
    void getPostsPages() throws Exception {

        PostResponse postResponse = new PostResponse(userPost);
        PostResponse postResponseOtherUser = new PostResponse(otherPost);
        List<PostResponse> postResponses = List.of(postResponse, postResponseOtherUser);

        System.out.println(postResponse);
        System.out.println(postResponseOtherUser);
        given(postService.fetchPostPagesBy(anyLong(), any())).willReturn(postResponses);


        mockMvc.perform(get("/posts").contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[1].id").exists())
                .andExpect(jsonPath("$.data[1].content").exists())
                .andExpect(jsonPath("$.data[1].User").exists())
                .andExpect(jsonPath("$.data[1].Comments").exists())
                .andExpect(jsonPath("$.data[1].Images").exists())
                .andExpect(jsonPath("$.data[1].Likers").exists())
                .andDo(print());

        verify(postService).fetchPostPagesBy(any(),any());
    }

    @Test
    @DisplayName("[API][POST][getPostsPagesByUser] 다른 유저 Posts 가져오기")
    void getPostsPagesByUser() throws Exception {

        PostResponse postResponseOtherUser = new PostResponse(otherPost);
        List<PostResponse> postResponses = List.of(postResponseOtherUser);
        given(postService.fetchPostPagesByUser(any(), any(), any())).willReturn(postResponses);

        mockMvc.perform(get("/posts/user/{userId}", otherUser.getUserId()).contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].id").exists())
                .andExpect(jsonPath("$.data[0].content").exists())
                .andExpect(jsonPath("$.data[0].User").exists())
                .andExpect(jsonPath("$.data[0].Comments").exists())
                .andExpect(jsonPath("$.data[0].Images").exists())
                .andExpect(jsonPath("$.data[0].Likers").exists())
                .andDo(print());

        verify(postService).fetchPostPagesByUser(any(),any(),any());

    }
}