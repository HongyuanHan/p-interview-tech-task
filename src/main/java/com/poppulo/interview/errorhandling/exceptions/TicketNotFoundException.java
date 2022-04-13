package com.poppulo.interview.errorhandling.exceptions;

import com.poppulo.interview.errorhandling.RestErrorCode;
import org.springframework.http.HttpStatus;

public class TicketNotFoundException extends AbstractRestException {
    public TicketNotFoundException(final String errorMessage, final Object... errorArgs) {
        super(HttpStatus.NOT_FOUND, RestErrorCode.TICKET_NOT_FOUND, errorMessage, errorArgs);
    }
}
