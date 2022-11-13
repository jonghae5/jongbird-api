package com.jonghae5.jongbirdapi.service;

import com.jonghae5.jongbirdapi.domain.Follow;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.exception.user.*;
import com.jonghae5.jongbirdapi.repository.follow.FollowRepository;
import com.jonghae5.jongbirdapi.repository.user.UserRepository;
import com.jonghae5.jongbirdapi.view.follow.FollowResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    private static User loginUser;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FollowRepository followRepository;

    @InjectMocks
    private FollowService followService;

    @BeforeAll
    static void setup() {
        loginUser =
                User.builder()
                        .userId(1L)
                        .nickname("test Nickname")
                        .email("test1@naver.com")
                        .password("testEncodePW1@")
                        .build();
    }

    @Test
    @DisplayName("팔로우 등록 성공")
    void addFollower() {
        //given
        Long followId = 50L;
        User followUser = User.builder()
                .userId(followId)
                .nickname("followNickname")
                .email("followTestEmail@naver.com")
                .password("followPassword1@")
                .build();
        when(userRepository.findById(followId)).thenReturn(Optional.of(followUser));
//        when(userRepository.findById(loginUser.getUserId())).thenReturn(Optional.of(loginUser));
        //when
        FollowResponse followResponse = followService.addFollower(loginUser, followId);
        assertEquals(followResponse.getUserId(), followId);
        verify(followRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("팔로우 등록 실패 (팔로우 X)")
    void addFollowerExFollower() {
        Long followId = 50L;
        when(userRepository.findById(followId)).thenReturn(Optional.ofNullable(null));
        assertThrows(InvalidateFollowException.class, () ->followService.addFollower(loginUser, followId));
    }

    @Test
    @DisplayName("팔로워 삭제 성공")
    void deleteFollower() {
        //given
        Long followId = 50L;
        User followUser = User.builder()
                .userId(followId)
                .nickname("followNickname")
                .email("followTestEmail@naver.com")
                .password("followPassword1@")
                .build();
        when(userRepository.findById(followId)).thenReturn(Optional.of(followUser));

        Follow follow = Follow.builder()
                .followId(888L)
                .following(loginUser)
                .follower(followUser)
                .build();

        when(followRepository.findByFollowingAndFollower(loginUser, followUser)).thenReturn(Optional.ofNullable(follow));

        //when
        FollowResponse followResponse = followService.deleteFollower(loginUser, followId);
        //then
        assertEquals(followResponse.getUserId(), followId);
        verify(followRepository, times(1)).deleteByFollowingAndFollower(any(), any());

    }

    @Test
    @DisplayName("팔로워 삭제 실패(팔로우X)")
    void deleteFollowerExFollow() {
        Long followId = 50L;
        when(userRepository.findById(followId)).thenReturn(Optional.ofNullable(null));
        assertThrows(InvalidateUnFollowException.class, () ->followService.deleteFollower(loginUser, followId));
    }

    @Test
    @DisplayName("팔로잉 삭제(차단)")
    void deleteFollowing() {
        //given
        Long followingId = 50L;
        User followingUser = User.builder()
                .userId(followingId)
                .nickname("followNickname")
                .email("followTestEmail@naver.com")
                .password("followPassword1@")
                .build();
        when(userRepository.findById(followingId)).thenReturn(Optional.of(followingUser));

        Follow follow = Follow.builder()
                .followId(888L)
                .following(loginUser)
                .follower(followingUser)
                .build();

        when(followRepository.findByFollowingAndFollower(loginUser, followingUser)).thenReturn(Optional.ofNullable(follow));


        //when
        FollowResponse followResponse = followService.deleteFollowing(loginUser, followingUser.getUserId());
        //then
        assertEquals(followResponse.getUserId(), followingId);
        verify(followRepository, times(1)).deleteByFollowingAndFollower(any(), any());
    }

    @Test
    @DisplayName("팔로잉 삭제 실패(팔로잉X)")
    void deleteFollowingExFollowing() {
        Long followingId = 50L;
        when(userRepository.findById(followingId)).thenReturn(Optional.ofNullable(null));
        assertThrows(InvalidateUserBlockException.class, () ->followService.deleteFollowing(loginUser, followingId));
    }



    @Test
    @DisplayName("팔로워 삭제 실패(팔로우 데이터 존재 X)")
    void deleteFollowerExFollowDataUnExist() {
        Long followerId = 50L;
        User followingUser = User.builder()
                .userId(followerId)
                .nickname("followNickname")
                .email("followTestEmail@naver.com")
                .password("followPassword1@")
                .build();

        when(userRepository.findById(followerId)).thenReturn(Optional.ofNullable(followingUser));
        when(followRepository.findByFollowingAndFollower(loginUser, followingUser)).thenReturn(Optional.ofNullable(null));
        assertThrows(InvalidateFollowUnExistException.class, () ->followService.deleteFollower(loginUser, followerId));
    }

    @Test
    @DisplayName("팔로잉 삭제 실패(팔로우 데이터 존재 X)")
    void deleteFollowingExFollowDataUnExist() {
        Long followingId = 50L;
        User followingUser = User.builder()
                .userId(followingId)
                .nickname("followNickname")
                .email("followTestEmail@naver.com")
                .password("followPassword1@")
                .build();

        when(userRepository.findById(followingId)).thenReturn(Optional.ofNullable(followingUser));
        when(followRepository.findByFollowingAndFollower(loginUser, followingUser)).thenReturn(Optional.ofNullable(null));
        assertThrows(InvalidateFollowUnExistException.class, () ->followService.deleteFollowing(loginUser, followingId));
    }

    @Test
    @DisplayName("팔로워 등록 실패(팔로우 데이터 존재 O)")
    void addFollowExFollowDataExist() {
        Long followerId = 50L;
        User followerUser = User.builder()
                .userId(followerId)
                .nickname("followNickname")
                .email("followTestEmail@naver.com")
                .password("followPassword1@")
                .build();

        Follow follow = Follow.builder()
                .followId(888L)
                .following(loginUser)
                .follower(followerUser)
                .build();

        when(userRepository.findById(followerId)).thenReturn(Optional.ofNullable(followerUser));
        when(followRepository.findByFollowingAndFollower(loginUser, followerUser)).thenReturn(Optional.ofNullable(follow));

        assertThrows(InvalidateFollowExistException.class, () ->followService.addFollower(loginUser, followerUser.getUserId()));
    }

}