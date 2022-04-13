package com.poppulo.interview.errorhandling.exceptions;

import com.poppulo.interview.errorhandling.RestErrorCode;
import org.springframework.http.HttpStatus;

public class EmptyLineListException extends AbstractRestException {
    public EmptyLineListException(String errorMessage, Object... errorArgs) {
        super(HttpStatus.BAD_REQUEST, RestErrorCode.EMPTY_LINE_LIST, errorMessage, errorArgs);
    }
}
