package com.poppulo.interview.errorhandling.exceptions;

import com.poppulo.interview.errorhandling.RestErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidInputException extends AbstractRestException {
    public InvalidInputException(String errorMessage, Object... errorArgs) {
        super(HttpStatus.BAD_REQUEST, RestErrorCode.INVALID_INPUT, errorMessage, errorArgs);
    }

    public InvalidInputException(RestErrorCode errorCode, String errorMessage, Object... errorArgs) {
        super(HttpStatus.BAD_REQUEST, errorCode, errorMessage, errorArgs);
    }
}
