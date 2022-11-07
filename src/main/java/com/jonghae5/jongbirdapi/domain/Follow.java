package com.jonghae5.jongbirdapi.domain;

import javax.persistence.*;

@Entity
public class Follow extends BaseTimeEntity{
    @Id @GeneratedValue
    private Long followId;

    @ManyToOne
    @JoinColumn(name = "followerId")
    private User follower;
    @ManyToOne
    @JoinColumn(name = "followingId")
    private User following;
}
