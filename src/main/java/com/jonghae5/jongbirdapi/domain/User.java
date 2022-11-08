package com.jonghae5.jongbirdapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class User extends BaseTimeEntity{

    @Id @GeneratedValue
    private Long userId;

    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String nickname;
    @Column(nullable = false)
    private String password;

//    @Builder.Default
//    @OneToMany(mappedBy = "user")
//    private List<Post> posts = new ArrayList<>();
//    @Builder.Default
//    @OneToMany(mappedBy = "following")
//    private List<Follow> followings = new ArrayList<>();
//    @Builder.Default
//    @OneToMany(mappedBy = "follower")
//    private List<Follow> followers = new ArrayList<>();
}
