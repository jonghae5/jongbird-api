package com.jonghae5.jongbirdapi.view.result;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {

    boolean success;
    int code;
    String message;
}
