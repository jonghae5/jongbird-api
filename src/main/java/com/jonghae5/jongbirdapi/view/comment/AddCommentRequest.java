package com.jonghae5.jongbirdapi.view.comment;

import com.jonghae5.jongbirdapi.domain.Comment;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddCommentRequest {

    @NotBlank
    private String content;


    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .build();
    }
}
