package com.jonghae5.jongbirdapi.service;

import com.jonghae5.jongbirdapi.domain.Comment;
import com.jonghae5.jongbirdapi.domain.Image;
import com.jonghae5.jongbirdapi.domain.Post;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.exception.post.InvalidatePostException;
import com.jonghae5.jongbirdapi.exception.user.InvalidateUserException;
import com.jonghae5.jongbirdapi.repository.comment.CommentRepository;
import com.jonghae5.jongbirdapi.repository.follow.FollowRepository;
import com.jonghae5.jongbirdapi.repository.like.LikeRepository;
import com.jonghae5.jongbirdapi.repository.post.PostRepository;
import com.jonghae5.jongbirdapi.repository.user.UserRepository;
import com.jonghae5.jongbirdapi.view.comment.AddCommentRequest;
import com.jonghae5.jongbirdapi.view.comment.AddCommentResponse;
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
class CommentServiceTest {

    private static Image image1;
    private static Image image2;
    private static Post post;
    private String content = "#리액트 #노드 test content";
    private static User loginUser;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private CommentService commentService;

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
    @DisplayName("댓글 등록 성공")
    void addComment() {

        AddCommentRequest addCommentRequest = new AddCommentRequest();
        addCommentRequest.setContent("댓글 좋아요!!!");

        when(userRepository.findById(any())).thenReturn(Optional.of(loginUser));
        when(postRepository.findById(any())).thenReturn(Optional.of(post));

        Comment comment = Comment.builder()
                .commentId(10L)
                .content(addCommentRequest.getContent())
                .post(post)
                .user(loginUser)
                .build();
        when(commentRepository.save(any())).thenReturn(comment);

        //when
        AddCommentResponse addCommentResponse = commentService.addComment(addCommentRequest, loginUser, post.getPostId());

        //then

        verify(commentRepository, times(1)).save(any(Comment.class));
        assertEquals(addCommentResponse.getContent(), addCommentRequest.getContent());

    }


    @Test
    @DisplayName("댓글 등록 실패(User X) ")
    void addCommentExUser() {

        AddCommentRequest addCommentRequest = new AddCommentRequest();
        addCommentRequest.setContent("댓글 좋아요!!!");

        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        Comment comment = Comment.builder()
                .commentId(10L)
                .content(addCommentRequest.getContent())
                .post(post)
                .user(loginUser)
                .build();

        //when
        //then
        assertThrows(InvalidateUserException.class, () -> commentService.addComment(addCommentRequest, loginUser, post.getPostId()));
    }

    @Test
    @DisplayName("댓글 등록 실패(Post X) ")
    void addCommentExPost() {

        AddCommentRequest addCommentRequest = new AddCommentRequest();
        addCommentRequest.setContent("댓글 좋아요!!!");

        when(userRepository.findById(any())).thenReturn(Optional.of(loginUser));
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        Comment comment = Comment.builder()
                .commentId(10L)
                .content(addCommentRequest.getContent())
                .post(post)
                .user(loginUser)
                .build();

        //when
        //then
        assertThrows(InvalidatePostException.class, () -> commentService.addComment(addCommentRequest, loginUser, post.getPostId()));
    }
}


