package com.jonghae5.jongbirdapi.view.follow;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FollowResponse {

    @JsonProperty(value = "UserId")
    private Long userId;
}
