package com.jonghae5.jongbirdapi.view.follow;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jonghae5.jongbirdapi.domain.Follow;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.view.dto.UserOnlyNickname;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GetFollowingsResponse {

    @JsonProperty(value = "Followings")
    List<UserOnlyNickname> followings = new ArrayList<>();

    public GetFollowingsResponse(List<Follow> followings) {
        this.followings = followings.stream()
                .map(x -> new UserOnlyNickname(x.getFollowing()))
                .collect(Collectors.toList());
    }
}
