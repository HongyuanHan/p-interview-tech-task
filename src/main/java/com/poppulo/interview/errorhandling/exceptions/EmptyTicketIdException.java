package com.poppulo.interview.errorhandling.exceptions;

import com.poppulo.interview.errorhandling.RestErrorCode;
import org.springframework.http.HttpStatus;

public class EmptyTicketIdException extends AbstractRestException {
    public EmptyTicketIdException(String errorMessage, Object... errorArgs) {
        super(HttpStatus.BAD_REQUEST, RestErrorCode.EMPTY_TICKET_ID, errorMessage, errorArgs);
    }
}
