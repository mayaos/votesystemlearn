package com.eastcompeace.enums;
/**
 * 返回代码定义
 */
public enum RespCodeEnum implements IEnums {
    /** 公共返回代码*/
    COMMON_SUSSESS("0000", "请求成功"), 
    COMMON_ERROR("9999", "发生未知错误，请重试");
    
    private String respCode;
    private String respMsg;

    private RespCodeEnum(String respCode, String respMsg) {
        this.respCode = respCode;
        this.respMsg = respMsg;
    }

    @Override
    public String getCode() {
        return respCode;
    }

    @Override
    public String getMessage() {
        return respMsg;
    }
}
