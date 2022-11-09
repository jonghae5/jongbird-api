package com.jonghae5.jongbirdapi.view.follow;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jonghae5.jongbirdapi.domain.Follow;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.view.dto.UserOnlyNickname;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GetFollowersResponse {

    @JsonProperty(value = "Followers")
    List<UserOnlyNickname> followers = new ArrayList<>();


    public GetFollowersResponse(List<Follow> followers) {
        this.followers = followers.stream()
                .map(x -> new UserOnlyNickname(x.getFollower()))
                .collect(Collectors.toList());
    }
}
