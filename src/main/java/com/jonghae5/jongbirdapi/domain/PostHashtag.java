package com.jonghae5.jongbirdapi.domain;

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
public class PostHashtag extends BaseTimeEntity{
    @Id @GeneratedValue
    private Long postHashtagId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "HASHTAG_ID")
    private Hashtag hashtag;



    public void addPostHashtag(Post post, Hashtag hashtag) {
        this.post = post;
        this.hashtag = hashtag;
        post.getPostHashtags().add(this);
        hashtag.getPostHashtags().add(this);
    }

    public void deletePostAndHashtag() {
        this.post = null;
        this.hashtag = null;
        post.getPostHashtags().remove(this);
        hashtag.getPostHashtags().remove(this);
    }
}

