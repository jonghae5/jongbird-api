package com.jonghae5.jongbirdapi.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "images")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Image extends BaseTimeEntity{

    @Id @GeneratedValue
    private Long imageId;
    @Column(nullable = false)
    private String src;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;


    public void addPost(Post post) {
        this.post = post;
        post.getImages().add(this);
    }

    public void deletePost() {
        this.post.getImages().remove(this);
        this.post = null;
    }
}
