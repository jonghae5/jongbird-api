package com.jonghae5.jongbirdapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "posts")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post extends BaseTimeEntity{

    @Id @GeneratedValue
    private Long postId;

    @Column(nullable = false)
    private String content;




    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;


    @Builder.Default
    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();
//

    @Builder.Default
    @OneToMany(mappedBy = "post")
    private List<Image> images = new ArrayList<>();


    @Builder.Default
    @OneToMany(mappedBy = "post")
    private List<Like> likers = new ArrayList<>();

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "post")
    private List<PostHashtag> postHashtags = new ArrayList<>();

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "RETWEET_ID")
    private Retweet retweet;

    public void updateContent(String content) {
        this.content = content;
    }
}
