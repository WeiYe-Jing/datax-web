package com.wugui.datax.admin.exception;

/**
 * @description: 自定义异常
 * @author: jingwk
 * @date: 2019/11/17 17:21
 */
public class TokenIsExpiredException extends Exception{
    public TokenIsExpiredException() {
    }

    public TokenIsExpiredException(String message) {
        super(message);
    }

    public TokenIsExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenIsExpiredException(Throwable cause) {
        super(cause);
    }

    public TokenIsExpiredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
