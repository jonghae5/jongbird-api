package com.jonghae5.jongbirdapi.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User extends BaseTimeEntity{

    @Id @GeneratedValue
    private Long userId;

    @NotBlank
    private String email;
    @NotBlank
    private String nickname;
    @NotBlank
    private String password;

}
