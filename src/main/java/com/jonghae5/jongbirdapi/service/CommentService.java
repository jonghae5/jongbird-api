package com.jonghae5.jongbirdapi.service;

import com.jonghae5.jongbirdapi.domain.Comment;
import com.jonghae5.jongbirdapi.domain.Post;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.repository.comment.CommentRepository;
import com.jonghae5.jongbirdapi.repository.post.PostRepository;
import com.jonghae5.jongbirdapi.repository.user.UserRepository;
import com.jonghae5.jongbirdapi.view.comment.AddCommentRequest;
import com.jonghae5.jongbirdapi.view.comment.AddCommentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    public AddCommentResponse addComment(AddCommentRequest addCommentRequest, User loginUser, Long postId) {

        User user = userRepository.findById(loginUser.getUserId()).orElseThrow(IllegalArgumentException::new);
        Post post = postRepository.findById(postId).orElseThrow(IllegalArgumentException::new);
        Comment comment = Comment.builder()
                .content(addCommentRequest.getContent())
                .build();

        comment.addUserAndPost(user, post);
        Comment saveComment = commentRepository.save(comment);

        AddCommentResponse addCommentResponse = AddCommentResponse.builder().build();
        addCommentResponse.create(saveComment, user);
        return addCommentResponse;
    }
}
