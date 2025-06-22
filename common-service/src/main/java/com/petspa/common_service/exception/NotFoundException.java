package com.petspa.common_service.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final int httpStatus;
    private final String code;

    public NotFoundException(int httpStatus, String code, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.code = code;

    }

}
