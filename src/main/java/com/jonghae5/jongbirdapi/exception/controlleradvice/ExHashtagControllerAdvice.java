package com.jonghae5.jongbirdapi.exception.controlleradvice;

import com.jonghae5.jongbirdapi.exception.Exception;
import com.jonghae5.jongbirdapi.exception.hashtag.InvalidatePostWithHashtagException;
import com.jonghae5.jongbirdapi.view.result.CommonResult;
import com.jonghae5.jongbirdapi.view.result.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExHashtagControllerAdvice {

    private final ResponseService responseService;


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResult handleHashtagPostEx(InvalidatePostWithHashtagException e) {
        log.info(e.getMessage());
        return responseService.getErrorResult(Exception.INVALIDATE_POST_WITH_HASHTAG.getCode(), Exception.INVALIDATE_POST_WITH_HASHTAG.getMessage());
    }

}




