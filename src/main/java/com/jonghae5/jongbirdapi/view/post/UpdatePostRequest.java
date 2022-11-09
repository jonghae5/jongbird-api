package com.jonghae5.jongbirdapi.view.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdatePostRequest {

    @JsonProperty(value = "PostId")
    private Long postId;
    @NotBlank
    private String content;
}
