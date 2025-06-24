package com.petspa.common_service.exception;

import lombok.Getter;

@Getter
public class DuplicateException extends RuntimeException {
    private final String id;
    public DuplicateException(String id,String message) {
        super(message);
        this.id = id;
    }
}
