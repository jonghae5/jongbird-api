package com.jonghae5.jongbirdapi.view.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
@Data
public class CreateUserRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String nickname;

}
