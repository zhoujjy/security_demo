package com.zj.security_demo.handler;

import com.zj.security_demo.common.R;
import com.zj.security_demo.exception.BusinessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.zj.security_demo.common.ErrCodeEnume.UNKNOW_EXCEPTION;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public R doBusinessException(BusinessException ex){
        return new R(ex.getCode(),null,ex.getMessage());
    }

    //抛出认证异常，交给security处理
    @ExceptionHandler(AuthenticationException.class)
    public R doAuthenticationException(AuthenticationException ex){
        throw ex;
    }

    //抛出认证异常，交给security处理
    @ExceptionHandler(AccessDeniedException.class)
    public R doAccessDeniedException(AccessDeniedException ex){
        throw ex;
    }

    @ExceptionHandler(Throwable.class)
    public R doThrowable(Throwable t){
        t.printStackTrace();
        return new R(UNKNOW_EXCEPTION.getCode(),UNKNOW_EXCEPTION.getMsg());
    }

}
