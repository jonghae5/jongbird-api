package com.jonghae5.jongbirdapi.controller;

import com.jonghae5.jongbirdapi.argumentResolver.Login;
import com.jonghae5.jongbirdapi.domain.User;
import com.jonghae5.jongbirdapi.service.UserService;
import com.jonghae5.jongbirdapi.session.SessionConst;
import com.jonghae5.jongbirdapi.view.user.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    @GetMapping
    public UserWithoutPasswordResponse getMyUser(@Login User loginUser) {
        return userService.findUserWithoutPassword(loginUser.getUserId());
    }

    @GetMapping("/{userId}")
    public UserWithoutPasswordResponse getOtherUser(@Login User loginUser, @PathVariable Long userId) {
        return userService.findUserWithoutPassword(userId);
    }

    @PostMapping
    public JoinUserResponse signUp(@RequestBody @Valid CreateUserRequest createUserRequest, HttpServletRequest request) {

        User joinUser = userService.join(createUserRequest);
        HttpSession session = request.getSession();
//        session.setAttribute(SessionConst.LOGIN_USER, joinUser);
        return new JoinUserResponse(joinUser.getUserId());
    }


    @PostMapping("/login")
    public UserWithoutPasswordResponse login(@RequestBody @Valid LoginUserRequest loginUserRequest,
                                  HttpSession session) {
        User loginUser = userService.login(loginUserRequest);
        session.setAttribute(SessionConst.LOGIN_USER, loginUser);

        return userService.findUserWithoutPassword(loginUser.getUserId());

    }

    @PostMapping("/logout")
    public String signUp(@Login User loginUser,
                         HttpSession session) {
        //세션에 로그인 회원 정보 삭제
        if (session != null) {
            session.removeAttribute(SessionConst.LOGIN_USER);
            session.invalidate();
        }
        return "로그아웃 완료";
    }


    // TODO
    // user/1/follow  > PATCH, DELETE >
    // DELETE /user/follower/1
    //GET /user/followers
    //GET /user/followings
    //POST /user/nickname

}
