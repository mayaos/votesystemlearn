package com.eastcompeace.dto;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 返回的信息头,每一个dto对象须包含
 * 
 * @author caiyaonan
 */
public class HeadDto {
    // 响应代码
    private String rspCode;
    // 结果描述
    private String rspMessage;

    public HeadDto() {
    }

    public HeadDto(String commandID, String transactionID, String timestamp, String rspCode, String rspMessage) {
        super();
        this.rspCode = rspCode;
        this.rspMessage = rspMessage;
    }

    public String getRspCode() {
        return rspCode;
    }

    public void setRspCode(String rspCode) {
        this.rspCode = rspCode;
    }

    public String getRspMessage() {
        return rspMessage;
    }

    public void setRspMessage(String rspMessage) {
        this.rspMessage = rspMessage;
    }

    @Override
    public String toString() {
        return "HeadDto [rspCode=" + rspCode + ", rspMessage=" + rspMessage + "]";
    }
}
