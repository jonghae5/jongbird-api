package com.jonghae5.jongbirdapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment extends BaseTimeEntity{

    @Id @GeneratedValue
    private Long commentId;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    public void addUserAndPost(User user, Post post) {
        this.user = user;
        this.post = post;
        post.getComments().add(this);
    }

    public void deleteUserAndPost() {
        this.post.getComments().remove(this);
        this.user = null;
        this.post = null;

    }
}
