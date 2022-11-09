package com.jonghae5.jongbirdapi.service;

import com.jonghae5.jongbirdapi.domain.Follow;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.repository.follow.FollowRepository;
import com.jonghae5.jongbirdapi.repository.user.UserRepository;
import com.jonghae5.jongbirdapi.view.follow.FollowResponse;
import com.jonghae5.jongbirdapi.view.follow.GetFollowersResponse;
import com.jonghae5.jongbirdapi.view.follow.GetFollowingsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    public FollowResponse addFollower(User loginUser, Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);

        Follow follow = Follow.builder()
                .following(loginUser)
                .follower(findUser)
                .build();

        followRepository.save(follow);

        return new FollowResponse(userId);
    }

    public FollowResponse deleteFollower(User loginUser, Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
        followRepository.deleteByFollowingAndFollower(loginUser, findUser);

        return new FollowResponse(userId);
    }

    public FollowResponse deleteFollowing(User loginUser, Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
        followRepository.deleteByFollowingAndFollower(loginUser, findUser);

        return new FollowResponse(userId);
    }

    public GetFollowersResponse getFollowers(User loginUser, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Follow> followers = followRepository.findByFollower(loginUser, pageable);
        return new GetFollowersResponse(followers);
    }

    public GetFollowingsResponse getFollowings(User loginUser, int limit) {
        Pageable pageable  = PageRequest.of(0, limit);

        List<Follow> followings = followRepository.findByFollowing(loginUser, pageable );

        return new GetFollowingsResponse(followings);
    }
}
