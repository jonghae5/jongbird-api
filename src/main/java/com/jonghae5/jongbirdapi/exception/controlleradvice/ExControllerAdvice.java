package com.jonghae5.jongbirdapi.exception.controlleradvice;

import com.jonghae5.jongbirdapi.exception.Exception;
import com.jonghae5.jongbirdapi.exception.user.InvalidateUserException;
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
public class ExControllerAdvice {

    private final ResponseService responseService;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonResult userExHandle(InvalidateUserException e) {
        log.info(e.getMessage());
        return responseService.getErrorResult(Exception.INVALIDATE_USER.getCode(), Exception.INVALIDATE_USER.getMessage());
    }
}
