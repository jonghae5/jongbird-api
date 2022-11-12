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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    String testEmail = "dhwhdgo2368@gmail.com";
    String testPassword = "testPassword@1";
    String testEncodePw = "testEncodePw@1";
    String testNickname = "testNickname";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원가입")
    void join() {

        //given

        CreateUserRequest createUserRequest = new CreateUserRequest(testEmail, testPassword, testNickname);

        when(passwordEncoder.encode(any())).thenReturn(testEncodePw);

        User user = User.builder()
                .nickname(testNickname)
                .email(testEmail)
                .password(passwordEncoder.encode(testPassword))
                .build();

        Long fakeUserId = 11L;
        ReflectionTestUtils.setField(user, "userId", fakeUserId);

        when(userRepository.save(any())).thenReturn(user);
        //중복회원 방지
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.ofNullable(null));

        when(userRepository.findById(fakeUserId)).thenReturn(Optional.ofNullable(user));

        //when
        User newUser = userService.join(createUserRequest);

        //then
        User findUser = userRepository.findById(newUser.getUserId()).get();

        assertEquals(user.getUserId(), findUser.getUserId());
        assertEquals(user.getNickname(), findUser.getNickname());
        assertEquals(user.getEmail(), findUser.getEmail());

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
    @Test
    @DisplayName("로그인")
    void login() {

        User loginUser = User.builder()
                .nickname(testNickname)
                .email(testEmail)
                .password(testEncodePw)
                .build();
        when(userRepository.findByEmail(loginUser.getEmail())).thenReturn(Optional.ofNullable(loginUser));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail(testEmail);
        loginUserRequest.setPassword(testPassword);
        // when
        User findUser = userService.login(loginUserRequest);
        // then
        assertEquals(loginUser.getNickname(), findUser.getNickname());
    }

    @Test
    @DisplayName("로그인 예외(없는 이메일)")
    void loginExEmail() {

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail(testEmail);
        loginUserRequest.setPassword(testPassword);

        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.ofNullable(null));
        // when
        // then
        assertThrows(InvalidateEmailException.class, () -> userService.login(loginUserRequest));
    }

    @Test
    @DisplayName("로그인 예외(비밀번호)")
    void loginExPassword() {

        String testExPassword = "123123123123";
        User loginUser = User.builder()
                .nickname(testNickname)
                .email(testEmail)
                .password(testEncodePw)
                .build();

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail(testEmail);
        loginUserRequest.setPassword(testExPassword);

        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.ofNullable(loginUser));
        when(passwordEncoder.matches(testExPassword, testEncodePw)).thenReturn(false);

        // when
        // then
        assertThrows(InvalidatePasswordException.class, () -> userService.login(loginUserRequest));
    }


    @Test
    @DisplayName("닉네임 변경")
    void changeNickname() {
        // given

        User loginUser = User.builder()
                .nickname("testCurrentNickname")
                .email("loginTest@naver.com")
                .password("loginPassword")
                .build();

        when(userRepository.findByNickname(any())).thenReturn(Optional.ofNullable(null));
        when(userRepository.findById(loginUser.getUserId())).thenReturn(Optional.ofNullable(loginUser));

        //when
        User findUser = userService.changeNickname(loginUser, "newNickname");

        //then
        assertEquals("newNickname", findUser.getNickname());
    }

    @Test
    @DisplayName("닉네임 변경 예외(중복 닉네임)")
    void changeNicknameEx() {

        // given
        User repositoryUser = User.builder()
                .nickname(testNickname)
                .email(testEmail)
                .password(testPassword)
                .build();

        Long fakeUserId = 10L;
        ReflectionTestUtils.setField(repositoryUser, "userId", fakeUserId);

        when(userRepository.findByNickname(repositoryUser.getNickname())).thenReturn(Optional.ofNullable(repositoryUser));

        // when
        User loginUser = User.builder()
                .nickname("testCurrentNickname")
                .email("loginTest@naver.com")
                .password("loginPassword")
                .build();

        Long fakeLoginUserId = 11L;
        ReflectionTestUtils.setField(repositoryUser, "userId", fakeLoginUserId);
        String changeNickname = testNickname;

        // then
        assertThrows(DuplicateNicknameException.class, () -> userService.changeNickname(loginUser, testNickname));

    }
}