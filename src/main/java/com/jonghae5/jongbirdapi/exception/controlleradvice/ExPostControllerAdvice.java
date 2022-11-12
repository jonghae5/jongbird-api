package com.jonghae5.jongbirdapi.exception.controlleradvice;

import com.jonghae5.jongbirdapi.exception.Exception;
import com.jonghae5.jongbirdapi.exception.post.*;
import com.jonghae5.jongbirdapi.view.result.CommonResult;
import com.jonghae5.jongbirdapi.view.result.ResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExPostControllerAdvice {

    private final ResultService responseService;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonResult handleAlreadyRetweetPostEx(AlreadyRetweetPostException e) {
        log.info(e.getMessage());
        return responseService.getErrorResult(Exception.ALREADY_RETWEET_POST.getCode(), Exception.ALREADY_RETWEET_POST.getMessage());
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonResult handleMyRetweetEx(InvalidateMyRetweetException e) {
        log.info(e.getMessage());
        return responseService.getErrorResult(Exception.INVALIDATE_MY_RETWEET.getCode(), Exception.INVALIDATE_MY_RETWEET.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonResult handleFollowtEx(InvalidateFollowException e) {
        log.info(e.getMessage());
        return responseService.getErrorResult(Exception.INVALIDATE_FOLLOW.getCode(), Exception.INVALIDATE_FOLLOW.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonResult handleUnFollowtEx(InvalidateUnFollowException e) {
        log.info(e.getMessage());
        return responseService.getErrorResult(Exception.INVALIDATE_UNFOLLOW.getCode(), Exception.INVALIDATE_UNFOLLOW.getMessage());
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResult handlePostEx(InvalidatePostException e) {
        log.info(e.getMessage());
        return responseService.getErrorResult(Exception.INVALIDATE_POST.getCode(), Exception.INVALIDATE_POST.getMessage());
    }
}
