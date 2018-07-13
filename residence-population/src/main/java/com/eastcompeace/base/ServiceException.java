package com.eastcompeace.base;
import com.eastcompeace.enums.IEnums;

/**
 * 公用的Exception.
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String               code          = IEnums.IENUMS_FAIL_CODE;
    private String            message          = IEnums.IENUMS_FAIL_MESSAGE;
    private IEnums            enums;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
        this.setMessage(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.setMessage(message);
    }

    public ServiceException setMessage(String message) {
        this.message = message;
        return this;
    }

    public ServiceException setIEnums(IEnums enums) {
        this.enums = enums;
        return this;
    }

    public ServiceException(IEnums enums) {
        this.message = enums.getMessage();
        this.enums = enums;
    }

    public String getMessage() {
        return message;
    }

    public IEnums getEnums() {
        return enums;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
