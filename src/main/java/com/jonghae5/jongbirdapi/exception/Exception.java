package com.jonghae5.jongbirdapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import javax.management.loading.MLetContent;

@AllArgsConstructor
@Getter
public enum Exception {
    DUPLICATE_ID(403, "이미 사용 중인 아이디입니다."),

    INVALIDATE_USER(404, "해당 사용자가 존재하지 않습니다."),

    INVALIDATE_USER_BLOCK(403, "없는 사람을 차단할 수 없습니다."),

    INVALIDATE_FOLLOW(403, "없는 사람을 팔로우할 수 없습니다."),

    //TODO
    // NICKNAME
//    DUPLICATE_ID(403, "이미 사용 중인 아이디입니다."),

    INVALIDATE_UNFOLLOW(403, "없는 사람을 언팔로우할 수 없습니다."),

    INVALIDATE_POST(404, "해당 게시글이 존재하지 않습니다."),

    INVALIDATE_MY_RETWEET(403, "자신의 글은 리트윗할 수 없습니다."),

    ALREADY_RETWEET_POST(403, "이미 리트윗한 게시글입니다."),

    LOGIN(401, "로그인이 필요합니다."),
    LOGOUT(401, "로그아웃이 필요합니다."),

    INVALIDATE_EMAIL(403, "존재하지않는 이메일입니다!"),
    DUPLICATE_NICKNAME(403, "닉네임이 이미 존재합니다!"),

    INVALIDATE_PASSWORD(403, "비밀번호가 틀렸습니다."),

    INVALIDATE_POST_WITH_HASHTAG(404, "해쉬태그에 맞는 게시글이 없습니다."),

    INVALIDATE_FOLLOW_EXIST(403, "이미 팔로우 데이터가 존재합니다."),

    INVALIDATE_FOLLOW_UNEXIST(404, "팔로우 데이터가 존재하지 않습니다.");
    private final int code;
    private final String message;

}
