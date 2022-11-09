package com.jonghae5.jongbirdapi.view;

import lombok.Data;

@Data
public class Result<T> {

    private Boolean error;
    private T data;

    public Result(Boolean error, T data) {
        this.error = error;
        this.data = data;
    }
}
