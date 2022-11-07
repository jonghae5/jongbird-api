package com.jonghae5.jongbirdapi.domain;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hashtags")
public class Hashtag extends BaseTimeEntity{
    @Id @GeneratedValue
    private Long hashtagId;

    @NotBlank
    private String content;

    @OneToMany(mappedBy = "hashtag")
    private List<PostHashtag> postHashtags = new ArrayList<>();

}
