package com.poppulo.interview.errorhandling;

public enum RestErrorCode {

    UNKNOWN("UNKNOWN"),
    INVALID_INPUT("INVALID_INPUT"),
    TICKET_NOT_FOUND("TICKET_NOT_FOUND"),
    EMPTY_TICKET_ID("EMPTY_TICKET_ID"),
    EMPTY_LINE_LIST("EMPTY_LINE_LIST"),
    UNSUPPORTED_DIGIT_COUNT_IN_LINE("UNSUPPORTED_DIGIT_COUNT_IN_LINE"),
    INVALID_LINE_CONTENT("INVALID_LINE_CONTENT"),
    UNIQUE_TICKET_ID_REQUIRED("UNIQUE_TICKET_ID_REQUIRED"),
    INVALID_AMEND_AFTER_STATUS_CHECKED("INVALID_AMEND_AFTER_STATUS_CHECKED")
    ;

    private final String code;

    RestErrorCode(final String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
