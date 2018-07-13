package com.eastcompeace.enums;
public interface IEnums {
    public final String IENUMS_SUCCESS_CODE    = "0000";
    public final String IENUMS_SUCCESS_MESSAGE = "请求成功";
    public final String IENUMS_FAIL_CODE       = "9999";
    public final String IENUMS_FAIL_MESSAGE    = "发生未知错误，请重试";

    public String getCode();

    public String getMessage();
}
