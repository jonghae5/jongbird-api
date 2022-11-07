package com.jonghae5.jongbirdapi.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


// 네이밍 에러
@Entity
@Table(name = "likes")
public class Like extends BaseTimeEntity{

    @Id @GeneratedValue
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

}
