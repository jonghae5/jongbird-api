package com.jonghae5.jongbirdapi.view.result;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ResultService {

    public CommonResult getErrorResult(int code, String message) {
        CommonResult commonResult = new CommonResult();
        commonResult.setSuccess(false);
        commonResult.setCode(code);
        commonResult.setMessage(message);
        return commonResult;
    }
    public<T> SingleResult<T> getSingleResult(T data) {
        SingleResult singleResult = new SingleResult();
        singleResult.setData(data);

        return singleResult;
    }

    public<T> ListResult<T> getListResult(List<T> data) {
        ListResult listResult = new ListResult();
        listResult.setData(data);

        return listResult;
    }


    void setSuccessResponse(CommonResult result) {
        result.setCode(0);
        result.setSuccess(true);
        result.setMessage("SUCCESS");
    }
}
