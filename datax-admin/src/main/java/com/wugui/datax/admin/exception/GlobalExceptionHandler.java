package com.wugui.datax.admin.exception;

import com.baomidou.mybatisplus.extension.api.R;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by jwk on 2019/07/05.
 * 全局异常处理
 * @author Jing WenKai
 * @date 2019/07/05 11:57
 */
@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e){
        log.error("系统异常{0}",e);
        return R.failed(e.getMessage());
    }
}
