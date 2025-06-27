package petspa.identity_service.advise;

import com.petspa.common_service.advice.CommonControllerAdvice;
import com.petspa.common_service.advice.RestError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import petspa.identity_service.exception.UserProfileException;

import java.net.URI;
@RestControllerAdvice
public class IdentityControllerAdvice implements CommonControllerAdvice {

    @ExceptionHandler(UserProfileException.class)
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    RestError userProfileExceptionHandler(UserProfileException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.SERVICE_UNAVAILABLE.value())
                .type(URI.create("https://problems.affina.com.vn/connection-error"))
                .title("User Profile Error")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("USER_PROFILE_ERROR")
                .build();
    }
}
