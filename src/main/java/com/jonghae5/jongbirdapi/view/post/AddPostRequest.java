package com.jonghae5.jongbirdapi.view.post;

import com.jonghae5.jongbirdapi.domain.Post;
import com.jonghae5.jongbirdapi.domain.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class AddPostRequest {

    @Builder.Default
    private List<String> imagePaths = new ArrayList<>();
    @NotBlank
    private String content;



    public Post toEntity(User user) {
        return Post.builder()
                .content(content)
                .user(user)
                .build();
    }
}
