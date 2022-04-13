package com.poppulo.interview.errorhandling;

import com.poppulo.interview.errorhandling.exceptions.AbstractRestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class RestExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(AbstractRestException.class)
    public ResponseEntity<RestErrorDTO> restExceptionHandler(final AbstractRestException e) {
        LOG.error(e.getMessage(), e);
        final RestErrorDTO errorDTO = new RestErrorDTO(e.getMessage(), e.getErrorCode());
        return new ResponseEntity<>(errorDTO, e.getStatusCode());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestErrorDTO> defaultExceptionHandler(final Exception e) {
        LOG.error(e.getMessage(), e);
        final RestErrorDTO errorDTO = new RestErrorDTO("Unknown error happened, please check the logs for details. " + "\n " + e.getMessage(), RestErrorCode.UNKNOWN);
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RestErrorDTO> httpMessageNotReadableExceptionHandler(final HttpMessageNotReadableException e) {
        LOG.error(e.getMessage(), e);
        final RestErrorDTO errorDTO = new RestErrorDTO("HttpMessageNotReadableException " + "\n " + e.getMessage(), RestErrorCode.UNKNOWN);
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

}
