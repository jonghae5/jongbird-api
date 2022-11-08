package com.jonghae5.jongbirdapi.repository.post;

import com.jonghae5.jongbirdapi.domain.Post;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.repository.user.UserRepository;
import com.jonghae5.jongbirdapi.service.UserService;
import com.jonghae5.jongbirdapi.view.user.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostQueryRepositoryTest {

    @Autowired
    PostQueryRepository postQueryRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    EntityManager em;




    @Test
    void 테스트() {
        em.createQuery("select p from Post p " +
                        "join fetch p.user u " +
                "where p.postId <:id order by p.createdAt DESC", Post.class)
                .setParameter("id", 100L)
                .getResultList();
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

        postQueryRepository.findByPostIdLessThanOrderByCreatedAtDesc(100L, 10);
    }
}