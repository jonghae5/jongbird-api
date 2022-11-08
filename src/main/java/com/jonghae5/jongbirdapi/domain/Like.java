package com.jonghae5.jongbirdapi.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;


// 네이밍 에러
@Entity
@Table(name = "likes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Like extends BaseTimeEntity{

    @Id @GeneratedValue
    private Long likeId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;


    public void addUserAndPost(User user, Post post) {
        this.user = user;
        this.post = post;
        post.getLikers().add(this);
    }

    public void deleteUserAndPost(User user, Post post) {
        this.user = null;
        this.post = null;
        post.getLikers().remove(this);
    }
}
