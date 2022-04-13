package com.poppulo.interview.errorhandling;

import java.util.Date;

public class RestErrorDTO {

    private final String message;

    private final RestErrorCode errorCode;

    private final Date timestamp;

    public RestErrorDTO(String message, RestErrorCode errorCode, final Object... args) {
        this.timestamp = new Date();
        this.message = String.format(message, args);
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public RestErrorCode getErrorCode() {
        return errorCode;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
