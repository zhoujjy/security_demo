package com.zj.security_demo.common;

public enum ErrCodeEnume {
    //这里相当于对象
    UNKNOW_EXCEPTION(10000,"系统未知异常"),
    VAILD_EXCEPTION(10001,"参数格式校验失败"),

    //登录
    USERNAME_ERR(10002,"用户不存在"),
    USERNAMEORPASSWORD_ERR(10003,"用户名或密码错误"),
    USERNOTLOGIN_ERR(10004,"用户未登录"),
    TOKEN_ERR(10005,"token不合法"),


    ;

    private Integer code;
    private String msg;
    ErrCodeEnume(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
