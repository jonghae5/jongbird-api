package com.jonghae5.jongbirdapi.service;

import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.exception.Exception;
import com.jonghae5.jongbirdapi.exception.user.DuplicateIdException;
import com.jonghae5.jongbirdapi.exception.user.DuplicateNicknameException;
import com.jonghae5.jongbirdapi.exception.user.InvalidateEmailException;
import com.jonghae5.jongbirdapi.exception.user.InvalidatePasswordException;
import com.jonghae5.jongbirdapi.repository.user.UserRepository;
import com.jonghae5.jongbirdapi.view.user.CreateUserRequest;
import com.jonghae5.jongbirdapi.view.user.LoginUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static User user;

    String testEmail = "dhwhdgo2368@gmail.com";
    String testPassword = "testPassword@1";
    String testNickname = "testNickname";

    @Mock
    private UserRepository userRepository;

    @Spy
    private PasswordEncoder passwordEncoder = new MockPasswordEncoder();

    @InjectMocks
    private UserService userService;


    @BeforeEach
    void setup() {
        user = User.builder()
                .nickname(testNickname)
                .email(testEmail)
                .password(passwordEncoder.encode(testPassword))
                .build();

        Long fakeUserId = 1L;
        ReflectionTestUtils.setField(user, "userId", fakeUserId);
    }
    @Nested
    @DisplayName("회원가입")
    class Join {
        @Test
        @DisplayName("성공")
        void success() {
            //given
            CreateUserRequest createUserRequest = new CreateUserRequest(testEmail, testPassword, testNickname);

            when(userRepository.save(any(User.class))).thenReturn(user);
            //중복회원 방지
            when(userRepository.findByEmail(testEmail)).thenReturn(Optional.ofNullable(null));

            //when
            User newUser = userService.join(createUserRequest);
            //then
            verify(userRepository, times(1)).findByEmail(testEmail);
            verify(userRepository, times(1)).save(any(User.class));
            assertEquals(user.getUserId(), newUser.getUserId());
            assertEquals(user.getNickname(), newUser.getNickname());
            assertEquals(user.getEmail(), newUser.getEmail());
            assertEquals(user.getPassword(), newUser.getPassword());
            assertTrue(passwordEncoder.matches(testPassword, newUser.getPassword()));
        }

        @Test
        @DisplayName("회원가입 예외(중복 이메일)")
        void duplicateIdEx() {
            String testEmail ="test1@naver.com";
            //given
            User user1 = User.builder()
                    .nickname("testNick1")
                    .email(testEmail)
                    .password("test1!@")
                    .build();
            when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.ofNullable(user1));
            //when
            CreateUserRequest newUserRequest = new CreateUserRequest(testEmail,"testpassword1","testNewNick");
            //then
            assertThrows(DuplicateIdException.class, () -> userService.join(newUserRequest));
        }
    }

    @Nested
    @DisplayName("로그인")
    class Login {

        private LoginUserRequest loginUserRequest;

        @BeforeEach
        void setup() {
            loginUserRequest = new LoginUserRequest();
            loginUserRequest.setEmail(testEmail);
            loginUserRequest.setPassword(testPassword);
        }

        @Test
        @DisplayName("성공")
        void success() {
            when(userRepository.findByEmail(loginUserRequest.getEmail())).thenReturn(Optional.ofNullable(user));
            // when
            User findUser = userService.login(loginUserRequest);
//            // then
            assertEquals(user.getNickname(), findUser.getNickname());
            assertEquals(user.getPassword(), findUser.getPassword());
        }

        @Test
        @DisplayName("로그인 예외(없는 이메일)")
        void exEmail() {
            when(userRepository.findByEmail(testEmail)).thenReturn(Optional.ofNullable(null));
            // when
            // then
            assertThrows(InvalidateEmailException.class, () -> userService.login(loginUserRequest));
        }

        @Test
        @DisplayName("로그인 예외(비밀번호)")
        void exPassword() {

            String testExPassword = "123123123123";
            loginUserRequest.setPassword(passwordEncoder.encode(testExPassword));
            when(userRepository.findByEmail(testEmail)).thenReturn(Optional.ofNullable(user));
            // when
            // then
            assertThrows(InvalidatePasswordException.class, () -> userService.login(loginUserRequest));
        }
    }


    @Nested
    @DisplayName("닉네임")
    class Nickname {
        @Test
        @DisplayName("닉네임 변경 성공")
        void changeNicknameSuccess() {
            // given
            when(userRepository.findByNickname(any())).thenReturn(Optional.ofNullable(null));
            when(userRepository.findById(user.getUserId())).thenReturn(Optional.ofNullable(user));
            //when
            User findUser = userService.changeNickname(user, "newNickname");
            //then
            assertEquals("newNickname", findUser.getNickname());
        }

        @Test
        @DisplayName("닉네임 변경 예외(중복 닉네임)")
        void changeNicknameEx() {

            // given
            when(userRepository.findByNickname(user.getNickname())).thenReturn(Optional.ofNullable(user));
            // when
            User loginUser = User.builder()
                    .nickname("testCurrentNickname")
                    .email("loginTest@naver.com")
                    .password("loginPassword")
                    .userId(2L)
                    .build();

            String changeNickname = testNickname;

            // then
            assertThrows(DuplicateNicknameException.class, () -> userService.changeNickname(loginUser, testNickname));

        }
    }



    private class MockPasswordEncoder implements PasswordEncoder {
        @Override
        public String encode(CharSequence rawPassword) {
            return new StringBuilder(rawPassword).reverse().toString();
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return encode(rawPassword).equals(encodedPassword);
        }
    }
}