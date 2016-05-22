package com.iscm.common.exception;

public class BusinessException  extends RuntimeException {

    private String message;
    private String code;
    public BusinessException(String message){
        super(message);
        this.message = message;
    }
    public BusinessException(String message, String code){
        super(message);
        this.message = message;
        this.code = code;
    }
    public BusinessException(String message, Exception e){
        super(message,e);
        this.message = message;

    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
