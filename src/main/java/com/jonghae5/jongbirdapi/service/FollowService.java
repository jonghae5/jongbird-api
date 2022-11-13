package com.jonghae5.jongbirdapi.service;

import com.jonghae5.jongbirdapi.domain.Follow;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.exception.user.*;
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
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    public FollowResponse addFollower(User loginUser, Long followUserId) {
        User findUser = userRepository.findById(followUserId).orElseThrow(InvalidateFollowException::new);


        Optional<Follow> exFollowData = followRepository.findByFollowingAndFollower(loginUser, findUser);
        if (exFollowData.isPresent()) {
            throw new InvalidateFollowExistException();
        }
        Follow follow = Follow.builder()
                .following(loginUser)
                .follower(findUser)
                .build();


        followRepository.save(follow);

        return new FollowResponse(followUserId);
    }

    public FollowResponse deleteFollower(User loginUser, Long followUserId) {
        User findUser = userRepository.findById(followUserId).orElseThrow(InvalidateUnFollowException::new);

        Optional<Follow> exFollowData = followRepository.findByFollowingAndFollower(loginUser, findUser);
        if (exFollowData.isEmpty()) {
            throw new InvalidateFollowUnExistException();
        }

        followRepository.deleteByFollowingAndFollower(loginUser, findUser);

        return new FollowResponse(followUserId);
    }

    public FollowResponse deleteFollowing(User loginUser, Long followingUserId) {
        User findUser = userRepository.findById(followingUserId).orElseThrow(InvalidateUserBlockException::new);

        Optional<Follow> exFollowData = followRepository.findByFollowingAndFollower(loginUser, findUser);
        if (exFollowData.isEmpty()) {
            throw new InvalidateFollowUnExistException();
        }

        followRepository.deleteByFollowingAndFollower(loginUser, findUser);

        return new FollowResponse(followingUserId);
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
