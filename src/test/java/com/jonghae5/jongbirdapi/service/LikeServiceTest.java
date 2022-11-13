package com.jonghae5.jongbirdapi.service;

import com.jonghae5.jongbirdapi.domain.Image;
import com.jonghae5.jongbirdapi.domain.Like;
import com.jonghae5.jongbirdapi.domain.Post;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.exception.post.InvalidatePostException;
import com.jonghae5.jongbirdapi.exception.user.InvalidateUserException;
import com.jonghae5.jongbirdapi.repository.like.LikeRepository;
import com.jonghae5.jongbirdapi.repository.post.PostRepository;
import com.jonghae5.jongbirdapi.repository.user.UserRepository;
import com.jonghae5.jongbirdapi.view.like.LikeResponse;
import org.junit.jupiter.api.*;
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
class LikeServiceTest {

    private static User loginUser;
    private static Image image1;
    private static Image image2;
    private static Post post;
    private String content = "#리액트 #노드 test content";

    @Mock
    private LikeRepository likeRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LikeService likeService;

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
    @DisplayName("좋아요 등록 성공")
    void addLike() {
        when(userRepository.findById(any())).thenReturn(Optional.of(loginUser));
        when(postRepository.findById(any())).thenReturn(Optional.of(post));

        //when
        LikeResponse likeResponse = likeService.addLike(loginUser, post.getPostId());

        //then
        verify(userRepository ,times(1)).findById(any());
        verify(postRepository ,times(1)).findById(any());
        verify(likeRepository ,times(1)).save(any());

        assertEquals(likeResponse.getPostId(), post.getPostId());
        assertEquals(likeResponse.getUserId(), post.getUser().getUserId());
    }


    @Test
    @DisplayName("좋아요 등록 실패(Post X)")
    void addLikeExPost() {
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        //when
        //then
        assertThrows(InvalidatePostException.class, ()-> likeService.addLike(loginUser,post.getPostId()));
    }

    @Test
    @DisplayName("좋아요 등록 실패(User X)")
    void addLikeExUser() {
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post));
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        //when
        //then
        assertThrows(InvalidateUserException.class, ()-> likeService.addLike(loginUser,post.getPostId()));
    }


    @Test
    @DisplayName("좋아요 취소 성공")
    void deleteLike() {
        when(userRepository.findById(any())).thenReturn(Optional.of(loginUser));
        when(postRepository.findById(any())).thenReturn(Optional.of(post));
        Like like = Like.builder()
                .likeId(10000L)
                .post(post)
                .user(loginUser)
                .build();
        when(likeRepository.findByUserAndPost(any(), any())).thenReturn(Optional.of(like));

        //when
        LikeResponse likeResponse = likeService.deleteLike(loginUser, post.getPostId());

        //then
        verify(userRepository ,times(1)).findById(any());
        verify(postRepository ,times(1)).findById(any());
        verify(likeRepository ,times(1)).findByUserAndPost(any(), any());
        verify(likeRepository ,times(1)).delete(any());

        assertEquals(likeResponse.getPostId(), post.getPostId());
        assertEquals(likeResponse.getUserId(), post.getUser().getUserId());
    }


    @Test
    @DisplayName("좋아요 취소 실패(Post X)")
    void deleteLikeExPost() {
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        assertThrows(InvalidatePostException.class, ()-> likeService.deleteLike(loginUser, post.getPostId()));
    }

    @Test
    @DisplayName("좋아요 취소 실패(User X)")
    void deleteLikeExUser() {
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post));
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        assertThrows(InvalidateUserException.class, ()-> likeService.deleteLike(loginUser, post.getPostId()));
    }

    @Test
    @DisplayName("좋아요 취소 실패(Like Data X)")
    void deleteLikeExLikeDataUnExust() {
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post));
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(loginUser));
        when(likeRepository.findByUserAndPost(any(),any())).thenReturn(Optional.ofNullable(null));

        assertThrows(IllegalArgumentException.class, ()-> likeService.deleteLike(loginUser, post.getPostId()));
    }

}