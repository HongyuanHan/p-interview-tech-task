package com.poppulo.interview.errorhandling.exceptions;

import com.poppulo.interview.errorhandling.RestErrorCode;
import org.springframework.http.HttpStatus;

public class UniqueTicketIdConstraintException extends AbstractRestException {
    public UniqueTicketIdConstraintException(String errorMessage, Object... errorArgs) {
        super(HttpStatus.BAD_REQUEST, RestErrorCode.UNIQUE_TICKET_ID_REQUIRED, errorMessage, errorArgs);
    }
}
