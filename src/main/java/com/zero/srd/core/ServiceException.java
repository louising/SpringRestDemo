package com.zero.srd.core;

public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String errCode;
    private Object[] args;
    private String data;

    public ServiceException() {
    }

    public ServiceException(String errCode, Object... args) {
        this.errCode = errCode;
        this.args = args;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}