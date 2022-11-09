package com.jonghae5.jongbirdapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Retweet extends BaseTimeEntity{
    @Id @GeneratedValue
    private Long retweetId;

    @JsonIgnore
    @OneToOne(fetch = LAZY, mappedBy = "retweet")
    private Post post;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;


}
