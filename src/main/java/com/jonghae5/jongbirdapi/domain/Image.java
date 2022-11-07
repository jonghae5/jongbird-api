package com.jonghae5.jongbirdapi.domain;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "images")
public class Image extends BaseTimeEntity{

    @Id @GeneratedValue
    private Long imageId;
    @NotBlank
    private String src;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;
}
