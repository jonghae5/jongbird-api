package com.jonghae5.jongbirdapi.view.dto;


import lombok.Data;

@Data
public class PostOnlyId {
    private Long id;
    public PostOnlyId(Long id) {
        this.id = id;
    }
}
