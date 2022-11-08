package com.jonghae5.jongbirdapi.view.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginUserRequest {

    //TODO 로그인 제약
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
