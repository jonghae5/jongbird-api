package com.jonghae5.jongbirdapi.view.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChangeNicknameRequest {
    @NotBlank
    @JsonProperty("nickname")
    private String nickname;
}
