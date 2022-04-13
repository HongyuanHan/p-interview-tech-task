package com.poppulo.interview.errorhandling.exceptions;

import com.poppulo.interview.errorhandling.RestErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidAmendmentWithStatusCheckedException extends AbstractRestException {
    public InvalidAmendmentWithStatusCheckedException(String errorMessage, Object... errorArgs) {
        super(HttpStatus.CONFLICT, RestErrorCode.INVALID_AMEND_AFTER_STATUS_CHECKED, errorMessage, errorArgs);
    }
}
