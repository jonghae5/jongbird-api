package com.jonghae5.jongbirdapi.view.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UpdatePostResponse {

    @JsonProperty(value = "PostId")
    private Long postId;
    private String content;

}
