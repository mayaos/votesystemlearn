package com.eastcompeace.dto;
import java.io.Serializable;
/**
 * 请求返回的dto,数据包含消息头head和消息体data
 */
public class ReturnDto<T> implements Serializable {
    /**
     * 返回结果的消息头
     */
    private HeadDto header;
    /**
     * 返回结果的消息体
     */
    private T       body;

    public ReturnDto() {
        super();
    }

    public ReturnDto(HeadDto header, T body) {
        super();
        this.header = header;
        this.body = body;
    }

    public HeadDto getHeader() {
        return header;
    }

    public void setHeader(HeadDto header) {
        this.header = header;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ReturnDto [header=" + header + ", body=" + body + "]";
    }
}
