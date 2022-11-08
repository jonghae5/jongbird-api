package com.jonghae5.jongbirdapi.service;

import com.jonghae5.jongbirdapi.domain.Hashtag;
import com.jonghae5.jongbirdapi.domain.Post;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.repository.post.PostQueryRepository;
import com.jonghae5.jongbirdapi.repository.post.PostRepository;
import com.jonghae5.jongbirdapi.repository.user.UserRepository;
import com.jonghae5.jongbirdapi.view.post.PostResponse;
import com.jonghae5.jongbirdapi.view.user.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;


    @Autowired
    PostService postService;
    @Autowired
    EntityManager em;

    @Test
    void regex() {
        String content;
        content = "어쩌구 저쩌구~~!!@!   ##안드로이드 #개발자 #급구";
        Pattern MY_PATTERN = Pattern.compile("#(\\S+)");
        Matcher m = MY_PATTERN.matcher(content);
        List<String> tags = new ArrayList<String>();

        while (m.find()) {
            tags.add(m.group(1).replace("#",""));
        }

        System.out.println("본문에서 해쉬태그(#) 파싱하기");
        System.out.println("본문 => "+content);
        System.out.println("해쉬태그 어레이리스트 => "+tags.toString());
        System.out.println(tags.get(0));
    }
    @Test
    void pagination() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setNickname("닉네임");
        createUserRequest.setEmail("dhwhdgo2368");
        createUserRequest.setPassword("123");

        User user = userService.join(createUserRequest);

        postRepository.save(Post.builder().content("hello").user(user).build());
        postRepository.save(Post.builder().content("hello2").user(user).build());
        postRepository.save(Post.builder().content("hello3").user(user).build());
        em.flush();
        List<PostResponse> postResponses = postService.fetchPostPagesBy(100L, user);
        System.out.println("postResponses = " + postResponses);
    }
}