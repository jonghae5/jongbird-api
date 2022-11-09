package com.jonghae5.jongbirdapi;

import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.repository.user.UserRepository;
import com.jonghae5.jongbirdapi.service.UserService;
import com.jonghae5.jongbirdapi.view.user.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestInit {

//    @Value("${spring.profiles.active}")
//    private String key;
    private final InitService initService;


    @PostConstruct
    public void init() throws IOException {
            initService.initData();
//        if (key.equals("dev")) {
//        }
    }

    /**
     * 테스트용 데이터 서비스
     */
    @RequiredArgsConstructor
    @Component
    @Transactional
    static class InitService {

        private final EntityManager em;
        private final UserService userService;

        private final UserRepository userRepository;

        public void initData() {

            CreateUserRequest createUserRequest = new CreateUserRequest();
            createUserRequest.setNickname("테스트");
            createUserRequest.setEmail("dhwhdgo2368@gmail.com");
            createUserRequest.setPassword("skfgnxh1");
            User user = userService.join(createUserRequest);
            userService.findUserWithoutPassword(user.getUserId());

            CreateUserRequest createUserRequest2 = new CreateUserRequest();
            createUserRequest2.setNickname("테스트2");
            createUserRequest2.setEmail("wldwldl2368@gmail.com");
            createUserRequest2.setPassword("skfgnxh1");
            User user2 = userService.join(createUserRequest2);
            userService.findUserWithoutPassword(user2.getUserId());
            log.info("테스트 유저 생성");

        }

    }
}

