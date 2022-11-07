package com.jonghae5.jongbirdapi.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PostHashtag extends BaseTimeEntity{
    @Id @GeneratedValue
    private Long postHashtagId;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "HASHTAG_ID")
    private Hashtag hashtag;
}

