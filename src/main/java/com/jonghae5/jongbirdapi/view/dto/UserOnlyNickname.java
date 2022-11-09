package com.jonghae5.jongbirdapi.view.dto;

import com.jonghae5.jongbirdapi.domain.User;
import lombok.Data;

@Data
public class UserOnlyNickname {
    private Long id;
    private String nickname;


    public UserOnlyNickname(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public UserOnlyNickname(User user) {
        this.id = user.getUserId();
        this.nickname = user.getNickname();
    }
}