package com.jonghae5.jongbirdapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonghae5.jongbirdapi.domain.*;
import com.jonghae5.jongbirdapi.service.RetweetService;
import com.jonghae5.jongbirdapi.service.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(RetweetController.class)
@Import(SecurityConfig.class)
@MockBean(JpaMetamodelMappingContext.class)
class RetweetControllerTest {

    private static User user;
    private static User followerUser;
    private static User followingUser;
    private static Follow follower;
    private static Follow following;
    private static Comment comment;
    private static Post userPost;
    private static Image image1;
    private static Image image2;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RetweetService retweetService;

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


        followerUser = User.builder()
                .email("follower@naver.com")
                .password("follower@!@#123")
                .nickname("follower")
                .build();

        followingUser = User.builder()
                .email("following@naver.com")
                .password("following@!@#123")
                .nickname("following")
                .build();

        follower = Follow
                .builder()
                .following(user)
                .follower(followerUser)
                .build();

        following = Follow
                .builder()
                .following(followingUser)
                .follower(user)
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

        comment.addUserAndPost(user, userPost);
    }

    @Test
    @DisplayName("[API][POST][addRetweet] 게시글 리트윗")
    void addRetweet() throws Exception {

        Post originalPost = Post.builder()
                .postId(1111L)
                .content("original Post")
                .user(User.builder()
                        .userId(2222L)
                        .nickname("newUser")
                        .email("hello baby")
                        .build())
                .build();

        Retweet newRetweet = Retweet.builder()
                .post(originalPost)
                .user(user)
                .build();

        Post retweetNewPost = Post.builder()
                .postId(123123L)
                .user(user)
                .retweet(newRetweet)
                .content("retweet")
                .build();


        AddRetweetPostResponse addRetweetPostResponse = new AddRetweetPostResponse();
        addRetweetPostResponse.create(originalPost, retweetNewPost);
        given(retweetService.addRetweet(any(), any())).willReturn(addRetweetPostResponse);

        mockMvc.perform(post("/post/{postId}/retweet", originalPost.getPostId()).contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.content").exists())
                .andExpect(jsonPath("$.data.Images").exists())
                .andExpect(jsonPath("$.data.User").exists())
                .andExpect(jsonPath("$.data.Comments").exists())
                .andExpect(jsonPath("$.data.Likers").exists())
                .andDo(print());

        verify(retweetService).addRetweet(any(), any());


    }
}