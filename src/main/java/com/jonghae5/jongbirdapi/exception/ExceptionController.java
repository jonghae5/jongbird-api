package com.jonghae5.jongbirdapi.exception;


import com.jonghae5.jongbirdapi.exception.user.InvalidateUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/exception")
public class ExceptionController {

    @GetMapping
    public void getException() {
        throw new InvalidateUserException();
    }
}
