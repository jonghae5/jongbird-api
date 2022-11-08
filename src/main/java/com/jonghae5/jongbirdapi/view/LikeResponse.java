package com.jonghae5.jongbirdapi.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikeResponse {

    @JsonProperty(value = "PostId")
    private Long postId;
    @JsonProperty(value = "UserId")
    private Long userId;
}
