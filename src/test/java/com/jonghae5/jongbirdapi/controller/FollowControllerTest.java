package com.jonghae5.jongbirdapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonghae5.jongbirdapi.domain.*;
import com.jonghae5.jongbirdapi.service.FollowService;
import com.jonghae5.jongbirdapi.service.UserService;
import com.jonghae5.jongbirdapi.view.follow.FollowResponse;
import com.jonghae5.jongbirdapi.view.follow.GetFollowersResponse;
import com.jonghae5.jongbirdapi.view.follow.GetFollowingsResponse;
import com.jonghae5.jongbirdapi.view.result.ResultService;
import com.jonghae5.jongbirdapi.view.user.UserWithoutPasswordResponse;
import com.jonghae5.jongbirdapi.web.security.SecurityConfig;
import com.jonghae5.jongbirdapi.web.session.SessionConst;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(FollowController.class)
@Import(SecurityConfig.class)
@MockBean(JpaMetamodelMappingContext.class)
class FollowControllerTest {

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
    FollowService followService;

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
                .userId(222L)
                .email("follower@naver.com")
                .password("follower@!@#123")
                .nickname("follower")
                .build();

        followingUser = User.builder()
                .userId(333L)
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


    }

    @Test
    @DisplayName("[API][PATCH][addFollower] 팔로워 추가")
    void addFollower() throws Exception {

        FollowResponse followResponse = new FollowResponse(followerUser.getUserId());

        given(followService.addFollower(any(), any())).willReturn(followResponse);

        mockMvc.perform(patch("/user/{userId}/follow", followerUser.getUserId()).contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.UserId").exists())
                .andDo(print());
        verify(followService).addFollower(any(), any());

    }

    @Test
    @DisplayName("[API][DELETE][deleteFollower] 팔로워 삭제")
    void deleteFollower() throws Exception {

        FollowResponse followResponse = new FollowResponse(followerUser.getUserId());

        given(followService.deleteFollower(any(), any())).willReturn(followResponse);

        mockMvc.perform(delete("/user/{userId}/follow", followerUser.getUserId()).contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.UserId").exists())
                .andDo(print());
        verify(followService).deleteFollower(any(), any());


    }

    @Test
    @DisplayName("[API][DELETE][deleteFollowing] 팔로잉 차단")
    void deleteFollowing() throws Exception {

        FollowResponse followResponse = new FollowResponse(followingUser.getUserId());

        given(followService.deleteFollowing(any(), any())).willReturn(followResponse);

        mockMvc.perform(delete("/user/follower/{userId}", followingUser.getUserId()).contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.UserId").exists())
                .andDo(print());
        verify(followService).deleteFollowing(any(), any());

    }

    @Test
    @DisplayName("[API][GET][getFollowers] 팔로워 가져오기")
    void getFollowers() throws Exception {

        GetFollowersResponse getFollowersResponse = new GetFollowersResponse(List.of(follower));
        given(followService.getFollowers(any(), anyInt())).willReturn(getFollowersResponse);

        mockMvc.perform(get("/user/followers").contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.Followers").exists())
                .andDo(print());
        verify(followService).getFollowers(any(), anyInt());


    }

    @Test
    @DisplayName("[API][GET][getFollowing] 팔로잉 가져오기")
    void getFollowings() throws Exception {

        GetFollowingsResponse getFollowingsResponse = new GetFollowingsResponse(List.of(following));
        given(followService.getFollowings(any(), anyInt())).willReturn(getFollowingsResponse);

        mockMvc.perform(get("/user/followings").contentType(MediaType.APPLICATION_JSON)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.Followings").exists())
                .andDo(print());

        verify(followService).getFollowings(any(),  anyInt());


    }
}