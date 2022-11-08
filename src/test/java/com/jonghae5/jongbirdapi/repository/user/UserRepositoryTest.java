package com.jonghae5.jongbirdapi.repository.user;

import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.service.UserService;
import com.jonghae5.jongbirdapi.view.user.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @Test
    public void 유저전체데이터() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setNickname("닉네임");
        createUserRequest.setEmail("dhwhdgo2368");
        createUserRequest.setPassword("123");
        User user = userService.join(createUserRequest);

        userService.findUserWithoutPassword(user.getUserId());
    }

}