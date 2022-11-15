package com.jonghae5.jongbirdapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonghae5.jongbirdapi.domain.*;
import com.jonghae5.jongbirdapi.exception.controlleradvice.ExUserControllerAdvice;
import com.jonghae5.jongbirdapi.service.UserService;
import com.jonghae5.jongbirdapi.view.result.ResultService;
import com.jonghae5.jongbirdapi.view.user.*;
import com.jonghae5.jongbirdapi.web.security.SecurityConfig;
import com.jonghae5.jongbirdapi.web.session.SessionConst;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
@MockBean(JpaMetamodelMappingContext.class)
class UserControllerTest {

    private static User user;
    private static User followerUser;
    private static User followingUser;
    private static Follow follower;
    private static Follow following;
    private static Comment comment;
    private static Post userPost;
    private static Image image1;
    private static Image image2;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    ResultService resultService;

    private MockHttpSession session;
    @BeforeEach
    void setupBeforeEach() {
//        mockMvc = MockMvcBuilders
//                .standaloneSetup(UserController.class)
//                .setControllerAdvice(ExUserControllerAdvice.class)
//                .build();

        user = User.builder()
                .userId(1L)
                .email("testEmail@naver.com")
                .password("testPassword@!@#123")
                .nickname("testNickname")
                .build();


        session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_USER, user);


        followerUser = User.builder()
                .email("follower@naver.com")
                .password("follower@!@#123")
                .nickname("follower")
                .build();

        followingUser = User.builder()
                .email("following@naver.com")
                .password("following@!@#123")
                .nickname("following")
                .build();

        follower = Follow
                .builder()
                .following(user)
                .follower(followerUser)
                .build();

        following = Follow
                .builder()
                .following(followingUser)
                .follower(user)
                .build();

        image1 = Image.builder()
                .imageId(1L)
                .src("/test/image1.jpeg")
                .build();

        image2 = Image.builder()
                .imageId(2L)
                .src("/test/image2.jpeg")
                .build();

        comment = Comment.builder()
                .commentId(11L)
                .content("test Comment")
                .build();

        userPost = Post.builder()
                .content("test Content")
                .user(user)
                .images(List.of(image1,image2))
                .postId(10l)
                .build();

        comment.addUserAndPost(user, userPost);
    }
    @Test
    @DisplayName("[API][GET][getMyUser] 내 정보 가져오기")
    void getMyUser() throws Exception {
        UserWithoutPasswordResponse userWithoutPasswordResponse = new UserWithoutPasswordResponse();
        userWithoutPasswordResponse.create(user, List.of(userPost), List.of(follower), List.of(following));

        given(userService.findUserWithoutPassword(any())).willReturn(userWithoutPasswordResponse);

        mockMvc.perform(get("/user").contentType(MediaType.APPLICATION_JSON)
                        .session(session)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.email").exists())
                .andExpect(jsonPath("$.data.nickname").exists())
                .andExpect(jsonPath("$.data.Posts").exists())
                .andExpect(jsonPath("$.data.Followers").exists())
                .andExpect(jsonPath("$.data.Followings").exists())
                .andDo(print());

        verify(userService).findUserWithoutPassword(any());


    }

    @Test
    @DisplayName("[API][GET][getOtherUser] 다른 유저 정보 가져오기")
    void getOtherUser() throws Exception {
        UserWithoutPasswordResponse userWithoutPasswordResponse = new UserWithoutPasswordResponse();
        userWithoutPasswordResponse.create(user, List.of(userPost), List.of(follower), List.of(following));

        given(userService.findUserWithoutPassword(any())).willReturn(userWithoutPasswordResponse);

        // RequestParam 방식
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("userId", String.valueOf(user.getUserId()));

        mockMvc.perform(get("/user/{userId}", user.getUserId()).contentType(MediaType.APPLICATION_JSON)
                        .session(session)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.email").exists())
                .andExpect(jsonPath("$.data.nickname").exists())
                .andExpect(jsonPath("$.data.Posts").exists())
                .andExpect(jsonPath("$.data.Followers").exists())
                .andExpect(jsonPath("$.data.Followings").exists())
                .andDo(print());

        verify(userService).findUserWithoutPassword(any());
    }

    @Test
    @DisplayName("[API][POST][signUp] 회원가입")
    void signUp() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest("newUser1@naver.com", "newUser1@#$!", "newNickname");
        User newUser = User.builder()
                .nickname("newNickname")
                .userId(111L)
                .email("newUser1@naver.com")
                .password("newUser1@#$!")
                .build();

        JoinUserResponse joinUserResponse = new JoinUserResponse(newUser.getUserId());
        given(userService.join(createUserRequest)).willReturn(newUser);
        mockMvc.perform(post("/user", user.getUserId()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").exists())
                .andDo(print());

    }

    @Test
    @DisplayName("[API][POST][login] 로그인")
    void login() throws Exception {
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail(user.getEmail());
        loginUserRequest.setPassword(user.getPassword());

        UserWithoutPasswordResponse userWithoutPasswordResponse = new UserWithoutPasswordResponse();
        userWithoutPasswordResponse.create(user, List.of(userPost), List.of(follower), List.of(following));


        given(userService.login(loginUserRequest)).willReturn(user);
        given(userService.findUserWithoutPassword(user.getUserId())).willReturn(userWithoutPasswordResponse);

        // 세션이 생성되었다고 가정
//        session.setAttribute(SessionConst.LOGIN_USER, loginUser);

        mockMvc.perform(post("/user/login", user.getUserId()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.email").exists())
                .andExpect(jsonPath("$.data.nickname").exists())
                .andExpect(jsonPath("$.data.Posts").exists())
                .andExpect(jsonPath("$.data.Followers").exists())
                .andExpect(jsonPath("$.data.Followings").exists())
                .andDo(print());

    }


    @Test
    @DisplayName("[API][PATCH][changeNickname] 닉네임 변경")
    void changeNickname() throws Exception {
        ChangeNicknameRequest changeNicknameRequest = new ChangeNicknameRequest();
        changeNicknameRequest.setNickname("Change Nickname");

        ChangeNicknameResponse changeNicknameResponse = new ChangeNicknameResponse(changeNicknameRequest.getNickname());
        given(userService.changeNickname(user, changeNicknameRequest.getNickname())).willReturn(changeNicknameResponse);


        mockMvc.perform(patch("/user/nickname").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeNicknameRequest))
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.nickname").exists())
                .andDo(print());


    }
}