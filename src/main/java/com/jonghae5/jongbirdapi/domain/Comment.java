package com.jonghae5.jongbirdapi.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "comments")
public class Comment extends BaseTimeEntity{

    @Id @GeneratedValue
    private Long commentId;

    @NotBlank
    private String content;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;
}
