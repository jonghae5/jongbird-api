package com.jonghae5.jongbirdapi.domain;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Follow extends BaseTimeEntity{
    @Id @GeneratedValue
    private Long followId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "followerId")
    private User follower;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "followingId")
    private User following;
}
