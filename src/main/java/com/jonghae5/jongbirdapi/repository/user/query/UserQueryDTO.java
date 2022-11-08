package com.jonghae5.jongbirdapi.repository.user.query;

import com.jonghae5.jongbirdapi.domain.Follow;
import com.jonghae5.jongbirdapi.domain.Post;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

public class UserQueryDTO {

    private Long userId;

    private String email;
    private String nickname;
    private String password;

    private List<Post> posts;
    private List<Follow> follows;


}
