package com.jonghae5.jongbirdapi.service;

import com.jonghae5.jongbirdapi.domain.Image;
import com.jonghae5.jongbirdapi.domain.Post;
import com.jonghae5.jongbirdapi.domain.Retweet;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.exception.post.AlreadyRetweetPostException;
import com.jonghae5.jongbirdapi.exception.post.InvalidateMyRetweetException;
import com.jonghae5.jongbirdapi.repository.post.PostRepository;
import com.jonghae5.jongbirdapi.repository.retweet.RetweetRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RetweetServiceTest {

    private static User loginUser;
    private static Image image1;
    private static Image image2;
    private static Post post;
    private String content = "#리액트 #노드 test content";


    @Mock
    private RetweetRepository retweetRepository;
    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private RetweetService retweetService;


    @BeforeAll
    static void setupBeforeAll() {
        loginUser =
                User.builder()
                        .userId(1L)
                        .nickname("test Nickname")
                        .email("test1@naver.com")
                        .password("testEncodePW1@")
                        .build();
    }

    @BeforeEach
    void setupBeforeEach() {

        image1 = Image.builder()
                .imageId(1L)
                .src("/test/image1.jpeg")
                .build();

        image2 = Image.builder()
                .imageId(2L)
                .src("/test/image2.jpeg")
                .build();

        post = Post.builder()
                .content(content)
                .user(loginUser)
                .images(List.of(image1,image2))
                .postId(10l)
                .build();
    }


    @Test
    @DisplayName("리트윗 등록(리트윗한 적 없는 Original 게시물)")
    void addRetweet() {

        User newUser = User.builder()
                .userId(999L)
                .nickname("newUser")
                .email("newUser@gmail.com")
                .password("newUser123456!@#$%")
                .build();

        Post newPost = Post.builder()
                .content(content)
                .user(newUser)
                .images(List.of(image1,image2))
                .postId(10l)
                .retweet(null)
                .build();

        when(postRepository.findById(newPost.getPostId())).thenReturn(Optional.ofNullable(newPost));
        when(retweetRepository.findByUserAndPost(any(),any())).thenReturn(Optional.ofNullable(null));

        retweetService.addRetweet(loginUser, newPost.getPostId());

        verify(postRepository, times(1)).save(any());
        verify(retweetRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("리트윗 등록 실패(자신의 글 리트윗)")
    void addRetweetExMyPostRetweet() {

        Post newPost = Post.builder()
                .content(content)
                .user(loginUser)
                .images(List.of(image1,image2))
                .postId(10l)
                .retweet(null)
                .build();

        when(postRepository.findById(newPost.getPostId())).thenReturn(Optional.ofNullable(newPost));

        assertThrows(InvalidateMyRetweetException.class, () -> retweetService.addRetweet(loginUser, newPost.getPostId()));
        verify(postRepository, times(1)).findById(any());
    }


    @Test
    @DisplayName("리트윗 등록 실패(자신이 리트윗한 글을 리트윗)")
    void addRetweetExMyRetweetPostRetweet() {

        User newUser = User.builder()
                .userId(999L)
                .nickname("newUser")
                .email("newUser@gmail.com")
                .password("newUser123456!@#$%")
                .build();

        Post originalPost = Post.builder()
                .content(content)
                .user(newUser)
                .images(List.of(image1,image2))
                .postId(100l)
                .retweet(null)
                .build();

        Retweet newRetweet = Retweet.builder()
                .post(originalPost)
                .user(loginUser)
                .build();

        Post retweetPost = Post.builder()
                .content("retweet")
                .user(loginUser)
                .postId(101l)
                .retweet(newRetweet)
                .build();

        when(postRepository.findById(retweetPost.getPostId())).thenReturn(Optional.ofNullable(retweetPost));

        assertThrows(InvalidateMyRetweetException.class, () -> retweetService.addRetweet(loginUser, retweetPost.getPostId()));
        verify(postRepository, times(1)).findById(any());
    }


    @Test
    @DisplayName("리트윗 등록 실패(Original 게시글 리트윗한 적 있는 경우)")
    void addRetweetExOriginalPostRetweetHistory() {

        User newUser = User.builder()
                .userId(999L)
                .nickname("newUser")
                .email("newUser@gmail.com")
                .password("newUser123456!@#$%")
                .build();

        Post originalPost = Post.builder()
                .content(content)
                .user(newUser)
                .images(List.of(image1,image2))
                .postId(100l)
                .retweet(null)
                .build();

        Retweet retweet = Retweet.builder()
                .post(originalPost)
                .user(loginUser)
                .build();

        when(postRepository.findById(originalPost.getPostId())).thenReturn(Optional.ofNullable(originalPost));
        when(retweetRepository.findByUserAndPost(loginUser, originalPost)).thenReturn(Optional.of(retweet));

        //when
        //then
        assertThrows(AlreadyRetweetPostException.class, () -> retweetService.addRetweet(loginUser, originalPost.getPostId()));


    }

    @Test
    @DisplayName("리트윗 등록 실패(리트윗 게시글 리트윗한 적 있는 경우)")
    void addRetweetExRetweetPostRetweetHistory() {

        User newUser = User.builder()
                .userId(999L)
                .nickname("newUser")
                .email("newUser@gmail.com")
                .password("newUser123456!@#$%")
                .build();

        Post originalPost = Post.builder()
                .content(content)
                .user(newUser)
                .images(List.of(image1,image2))
                .postId(100l)
                .retweet(null)
                .build();

        User otherUser = User.builder()
                .userId(9999L)
                .nickname("otherUser")
                .email("otherUser@gmail.com")
                .password("otherUser123456!@#$%")
                .build();

        Retweet otherRetweet = Retweet.builder()
                .post(originalPost)
                .user(otherUser)
                .build();

        Post retweetPost = Post.builder()
                .content("retweet")
                .user(otherUser)
                .postId(101l)
                .retweet(otherRetweet)
                .build();

        Retweet retweet = Retweet.builder()
                .post(originalPost)
                .user(loginUser)
                .build();

        when(postRepository.findById(retweetPost.getPostId())).thenReturn(Optional.ofNullable(retweetPost));
        when(postRepository.findById(originalPost.getPostId())).thenReturn(Optional.ofNullable(originalPost));

        when(retweetRepository.findByUserAndPost(loginUser, originalPost)).thenReturn(Optional.of(retweet));

        //when
        //then
        assertThrows(AlreadyRetweetPostException.class, () -> retweetService.addRetweet(loginUser, retweetPost.getPostId()));


    }


}