package com.poppulo.interview.errorhandling.exceptions;

import com.poppulo.interview.errorhandling.RestErrorCode;
import org.springframework.http.HttpStatus;

public class LineContentInvalidException extends AbstractRestException {
    public LineContentInvalidException(String errorMessage, Object... errorArgs) {
        super(HttpStatus.BAD_REQUEST, RestErrorCode.INVALID_LINE_CONTENT, errorMessage, errorArgs);
    }
}
