package com.jonghae5.jongbirdapi.service;

import com.jonghae5.jongbirdapi.domain.Post;
import com.jonghae5.jongbirdapi.domain.Retweet;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.repository.retweet.RetweetRepository;
import com.jonghae5.jongbirdapi.repository.post.PostRepository;
import com.jonghae5.jongbirdapi.view.retweet.AddRetweetPostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RetweetService {
    private final RetweetRepository retweetRepository;
    private final PostRepository postRepository;

    public AddRetweetPostResponse addRetweet(User loginUser, Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(IllegalStateException::new);

        if (loginUser.getUserId() == post.getUser().getUserId()) {
            log.error("자신의 글은 리트윗할 수 없습니다.");
        }

        if ((post.getRetweet()!=null)  && (post.getRetweet().getUser().getUserId() == loginUser.getUserId())) {
            log.error("자신이 리트윗한 글은 리트윗할 수 없습니다.");
        }

        Long retweetPostTargetId = post.getPostId();

        // 리트윗한 흔적이 있다면
        if (Optional.ofNullable(post.getRetweet()).isPresent()) {
            retweetPostTargetId = post.getRetweet().getPost().getPostId();
        }

        // Original Post
        Post originalPost = postRepository.findById(retweetPostTargetId).orElseThrow(IllegalStateException::new);

        Optional<Retweet> findRetweetPost = retweetRepository.findByUserAndPost(loginUser,originalPost);
        if (findRetweetPost.isPresent()) {
            log.error("이미 리트윗한 게시글입니다.");
        }

        Retweet newRetweet = Retweet.builder()
                .post(originalPost)
                .user(loginUser)
                .build();

        Post retweetNewPost = Post.builder()
                .user(loginUser)
                .retweet(newRetweet)
                .content("retweet")
                .build();

        postRepository.save(retweetNewPost);
        retweetRepository.save(newRetweet);

        AddRetweetPostResponse addRetweetPostResponse = new AddRetweetPostResponse();
        addRetweetPostResponse.create(originalPost , retweetNewPost);
        return addRetweetPostResponse;
    }

}
