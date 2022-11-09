package com.jonghae5.jongbirdapi.controller;


import com.jonghae5.jongbirdapi.web.argumentResolver.Login;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.service.FollowService;
import com.jonghae5.jongbirdapi.view.follow.FollowResponse;
import com.jonghae5.jongbirdapi.view.follow.GetFollowersResponse;
import com.jonghae5.jongbirdapi.view.follow.GetFollowingsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class FollowController {

    private final FollowService followService;


    @PatchMapping("/{userId}/follow")
    public FollowResponse addFollower(@Login User loginUser, @PathVariable Long userId) {
        return followService.addFollower(loginUser, userId);
    }

    @DeleteMapping("/{userId}/follow")
    public FollowResponse deleteFollower(@Login User loginUser, @PathVariable Long userId) {
        return followService.deleteFollower(loginUser, userId);
    }

    @DeleteMapping("/follower/{userId}")
    public FollowResponse deleteFollowing(@Login User loginUser, @PathVariable Long userId) {
        return followService.deleteFollowing(loginUser, userId);
    }

    @GetMapping("/followers")
    public GetFollowersResponse getFollowers(@Login User loginUser, @RequestParam(defaultValue = "3") int limit) {
        return followService.getFollowers(loginUser, limit);

    }

    @GetMapping("/followings")
    public GetFollowingsResponse getFollowings(@Login User loginUser, @RequestParam(defaultValue = "3") int limit) {
        return followService.getFollowings(loginUser, limit);
    }
}
