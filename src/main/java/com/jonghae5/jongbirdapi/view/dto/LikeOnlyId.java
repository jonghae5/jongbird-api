package com.jonghae5.jongbirdapi.view.dto;


import com.jonghae5.jongbirdapi.domain.Like;
import lombok.Data;

@Data
public class LikeOnlyId {
    private Long id;

    public LikeOnlyId(Like like) {
        this.id = id;
    }
}