package petspa.identity_service.exception;


import petspa.identity_service.dto.response.ErrorResponse;

public class UserProfileException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public UserProfileException(ErrorResponse errorResponse) {

        this.errorResponse = errorResponse;
    }
    public UserProfileException(ErrorResponse errorResponse, String message) {
        super(message);
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }


}
