package com.jonghae5.jongbirdapi.service;

import com.jonghae5.jongbirdapi.domain.Like;
import com.jonghae5.jongbirdapi.domain.Post;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.repository.like.LikeRepository;
import com.jonghae5.jongbirdapi.repository.post.PostRepository;
import com.jonghae5.jongbirdapi.repository.user.UserRepository;
import com.jonghae5.jongbirdapi.view.like.LikeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    public LikeResponse addLike(User loginUser , Long postId) {
        log.info("addLike Service Post 호출");
        Post post = postRepository.findById(postId).orElseThrow(IllegalArgumentException::new);
        log.info("addLike Service User 호출");
        User user = userRepository.findById(loginUser.getUserId()).orElseThrow(IllegalArgumentException::new);

        log.info("user Nickname={}", user.getNickname());
        log.info("post content={}", post.getContent());
        Like like = Like.builder().build();
        like.addUserAndPost(user, post);
        Like saveLike = likeRepository.save(like);

        LikeResponse likeResponse = LikeResponse.builder()
                .postId(post.getPostId())
                .userId(user.getUserId())
                .build();
        return likeResponse;

    }

    public LikeResponse deleteLike(User loginUser , Long postId) {
        log.info("deleteLike Service Post & User 호출");
        Post post = postRepository.findById(postId).orElseThrow(IllegalArgumentException::new);
        User user = userRepository.findById(loginUser.getUserId()).orElseThrow(IllegalArgumentException::new);
        Like like = likeRepository.findByUserAndPost(user, post).orElseThrow(IllegalArgumentException::new);
        like.deleteUserAndPost();
        likeRepository.delete(like);

        LikeResponse likeResponse = LikeResponse.builder()
                .postId(post.getPostId())
                .userId(user.getUserId())
                .build();

        return likeResponse;

    }
}
