package com.jonghae5.jongbirdapi.view.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddCommentRequest {

    @NotBlank
    private String content;
}
