package com.poppulo.interview.errorhandling.exceptions;

import com.google.common.base.Strings;
import com.poppulo.interview.errorhandling.RestErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public class AbstractRestException extends RuntimeException {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractRestException.class);

    private final HttpStatus statusCode;

    private final RestErrorCode errorCode;

    public AbstractRestException(final HttpStatus statusCode, final RestErrorCode errorCode, final String errorMessage, final Object... errorArgs) {
        super(format(errorMessage, errorArgs));
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }

    private static String format(String errorMessage, Object[] errorArgs) {
        String msg = Strings.nullToEmpty(errorMessage);
        try {
            return String.format(msg, errorArgs);
        } catch (Exception e) {
            LOG.error("Get error while formatting parametrised error message '{}'", errorMessage, e);
            return msg + " - " + Arrays.toString(errorArgs);
        }
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public RestErrorCode getErrorCode() {
        return errorCode;
    }
}
