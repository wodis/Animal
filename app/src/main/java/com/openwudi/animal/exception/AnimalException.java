package com.openwudi.animal.exception;

/**
 * Created by diwu on 16/12/1.
 */

public class AnimalException extends RuntimeException {
    private int errorCode = RES_STATUS.FAILED.code;
    private String errorMsg = "unknown error";

    public AnimalException() {
        super();
    }

    public AnimalException(String message, int errorCode, String errorMsg) {
        super(message);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public AnimalException(String message, RES_STATUS status) {
        super(message);
        this.errorCode = status.code;
        this.errorMsg = status.msg;
    }

    public AnimalException(RES_STATUS status) {
        super(status.msg);
        this.errorCode = status.code;
        this.errorMsg = status.msg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
