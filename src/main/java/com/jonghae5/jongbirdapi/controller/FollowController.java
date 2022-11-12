package com.jonghae5.jongbirdapi.controller;


import com.jonghae5.jongbirdapi.view.result.ResultService;
import com.jonghae5.jongbirdapi.view.result.SingleResult;
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
    private final ResultService responseService;

    @PatchMapping("/{userId}/follow")
    public SingleResult<FollowResponse> addFollower(@Login User loginUser, @PathVariable Long userId) {
        return responseService.getSingleResult(followService.addFollower(loginUser, userId));
    }

    @DeleteMapping("/{userId}/follow")
    public SingleResult<FollowResponse> deleteFollower(@Login User loginUser, @PathVariable Long userId) {
        return responseService.getSingleResult(followService.deleteFollower(loginUser, userId));
    }

    @DeleteMapping("/follower/{userId}")
    public SingleResult<FollowResponse> deleteFollowing(@Login User loginUser, @PathVariable Long userId) {
        return responseService.getSingleResult(followService.deleteFollowing(loginUser, userId));
    }

    //TODO
    @GetMapping("/followers")
    public SingleResult<GetFollowersResponse> getFollowers(@Login User loginUser, @RequestParam(defaultValue = "3") int limit) {
        return responseService.getSingleResult(followService.getFollowers(loginUser, limit));

    }

    @GetMapping("/followings")
    public SingleResult<GetFollowingsResponse> getFollowings(@Login User loginUser, @RequestParam(defaultValue = "3") int limit) {
        return responseService.getSingleResult(followService.getFollowings(loginUser, limit));
    }
}
