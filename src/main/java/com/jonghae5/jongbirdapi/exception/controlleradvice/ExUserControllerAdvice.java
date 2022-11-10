package com.jonghae5.jongbirdapi.exception.controlleradvice;

import com.jonghae5.jongbirdapi.exception.Exception;
import com.jonghae5.jongbirdapi.exception.user.*;
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
public class ExUserControllerAdvice {


    private final ResponseService responseService;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResult handleUserEx(InvalidateUserException e) {
        log.info(e.getMessage());
        return responseService.getErrorResult(Exception.INVALIDATE_USER.getCode(), Exception.INVALIDATE_USER.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonResult handleUserBlockEx(InvalidateUserBlockException e) {
        log.info(e.getMessage());
        return responseService.getErrorResult(Exception.INVALIDATE_USER_BLOCK.getCode(), Exception.INVALIDATE_USER_BLOCK.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonResult handleDuplicateIdEx(DuplicateIdException e) {
        log.info(e.getMessage());
        return responseService.getErrorResult(Exception.DUPLICATE_ID.getCode(), Exception.DUPLICATE_ID.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResult handleLoginEx(LoginException e) {
        log.info(e.getMessage());
        return responseService.getErrorResult(Exception.LOGIN.getCode(), Exception.LOGIN.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResult handleLogoutEx(LogoutException e) {
        log.info(e.getMessage());
        return responseService.getErrorResult(Exception.LOGOUT.getCode(), Exception.LOGOUT.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonResult handleEmailEx(InvalidateEmailException e) {
        log.info(e.getMessage());
        return responseService.getErrorResult(Exception.INVALIDATE_EMAIL.getCode(), Exception.INVALIDATE_EMAIL.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonResult handlePasswordEx(InvalidatePasswordException e) {
        log.info(e.getMessage());
        return responseService.getErrorResult(Exception.INVALIDATE_PASSWORD.getCode(), Exception.INVALIDATE_PASSWORD.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonResult handleNicknameEx(DuplicateNicknameException e) {
        log.info(e.getMessage());
        return responseService.getErrorResult(Exception.DUPLICATE_NICKNAME.getCode(), Exception.DUPLICATE_NICKNAME.getMessage());
    }
}
