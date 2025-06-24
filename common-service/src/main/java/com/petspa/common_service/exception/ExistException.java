package com.petspa.common_service.exception;

import lombok.Getter;

@Getter
public class ExistException extends RuntimeException {
    private final String id;
    public ExistException(String id,String message) {
        super(message);
        this.id = id;
    }
}
