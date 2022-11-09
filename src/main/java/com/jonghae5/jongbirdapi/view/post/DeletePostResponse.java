package com.jonghae5.jongbirdapi.view.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeletePostResponse {

    @JsonProperty(value = "PostId")
    private Long postId;

    public DeletePostResponse(Long postId) {
        this.postId = postId;
    }
}
