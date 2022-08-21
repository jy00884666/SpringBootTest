package com.springboot.exception;

/**
 * 业务异常捕获类
 */
public class BusinessException extends RuntimeException {
    
    private static final long serialVersionUID = -1944973629369163147L;
    
    /**
     * 错误码
     */
    private String errorCode;
    
    /**
     * 错误描述
     */
    private String errorDesc;
    
    public BusinessException() {
        super();
    }
    
    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorDesc = message;
    }
    
    public BusinessException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorDesc = message;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getErrorDesc() {
        return errorDesc;
    }
    
    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }
    
}
