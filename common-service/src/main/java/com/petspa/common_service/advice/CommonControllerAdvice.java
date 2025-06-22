package com.petspa.common_service.advice;

import com.petspa.common_service.exception.ConnectionException;
import com.petspa.common_service.exception.NotFoundException;
import com.petspa.common_service.exception.ResourceNotFoundException;
import com.petspa.common_service.exception.UnknownValueException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.naming.AuthenticationException;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

public interface CommonControllerAdvice {
    Logger log = LoggerFactory.getLogger(CommonControllerAdvice.class);

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    default RestError handlingValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        Map<String, List<String>> groupedMessages = exception.getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(DefaultMessageSourceResolvable::getDefaultMessage, Collectors.toList())
                ));

        List<RestViolation> violations = groupedMessages.entrySet().stream()
                .map(entry -> new RestViolation(entry.getKey(), entry.getValue()))
                .toList();

        return RestError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .type(URI.create("https://problems.affina.com.vn/validation-error"))
                .title("Validation Failed")
                .detail("Invalid request. Please check the input data and try again.")
                .instance(URI.create(request.getRequestURI()))
                .code("validation_error")
                .violations(violations)
                .build();
    }

    @ExceptionHandler(ConnectionException.class)
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    default RestError connectionExceptionHandler(ConnectionException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.SERVICE_UNAVAILABLE.value())
                .type(URI.create("https://problems.affina.com.vn/connection-error"))
                .title("Connection Error")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("CONNECTION_ERROR")
                .build();
    }

    @ExceptionHandler(UnknownValueException.class)
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    default RestError unknowvalueExceptionHandler(UnknownValueException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.SERVICE_UNAVAILABLE.value())
                .type(URI.create("https://problems.affina.com.vn/connection-error"))
                .title("Unknow Value Error")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("UNKNOW_VALUE_ERROR")
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    default RestError accessDeniedExceptionHandler(AccessDeniedException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .type(URI.create("https://problems.affina.com.vn/access-denied"))
                .title("Access Denied")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("ACCESS_DENIED")
                .build();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    default RestError authenticationExceptionHandler(AuthenticationException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .type(URI.create("https://problems.affina.com.vn/authentication-error"))
                .title("Authentication Error")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("AUTHENTICATION_ERROR")
                .build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    default RestError resourceNotFoundExceptionHandler(ResourceNotFoundException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .type(URI.create("https://problems.affina.com.vn/resource-not-found"))
                .title("Resource Not Found")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("RESOURCE_NOT_FOUND")
                .build();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    default RestError noHandlerFoundExceptionHandler(NoHandlerFoundException e, HttpServletRequest request) {
        return RestError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .type(URI.create("https://problems.affina.com.vn/endpoint-not-found"))
                .title("Endpoint Not Found")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("ENDPOINT_NOT_FOUND")
                .build();
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    default RestError bindExceptionHandler(BindException e, HttpServletRequest request) {
        Map<String, RestViolation> violations = new HashMap<>();

        for (var error : e.getBindingResult().getFieldErrors()) {
            if (violations.containsKey(error.getField())) {
                violations.get(error.getField()).messages().add(error.getDefaultMessage());
            } else {
                violations.put(
                        error.getField(),
                        new RestViolation(error.getField(), new ArrayList<>(List.of(
                                Optional.ofNullable(error.getDefaultMessage())
                                        .orElse("This field is not valid")))));
            }
        }

        return RestError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .type(URI.create("https://problems.affina.com.vn/validation-error"))
                .title("Validation Error")
                .detail("Validation error on request object")
                .instance(URI.create(request.getRequestURI()))
                .code("VALIDATION_ERROR")
                .violations(violations.values().stream().toList())
                .build();
    }

    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            MissingServletRequestPartException.class,
            ServletRequestBindingException.class,
            AsyncRequestTimeoutException.class,
            ErrorResponseException.class,
            ConversionNotSupportedException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class
    })
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    default RestError handleException(Exception exception, HttpServletRequest request) {
        if (exception instanceof ConversionNotSupportedException e) {
            return RestError.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .type(URI.create("https://problems.affina.com.vn/conversion-not-supported"))
                    .title("Conversion Not Supported")
                    .detail(e.getMessage())
                    .instance(URI.create(request.getRequestURI()))
                    .code("CONVERSION_NOT_SUPPORTED")
                    .build();
        } else if (exception instanceof TypeMismatchException e) {
            return RestError.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .type(URI.create("https://problems.affina.com.vn/type-mismatch-error"))
                    .title("Type Mismatch Error")
                    .detail(e.getMessage())
                    .instance(URI.create(request.getRequestURI()))
                    .code("TYPE_MISMATCH_ERROR")
                    .build();
        } else if (exception instanceof HttpMessageNotReadableException e) {
            return RestError.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .type(URI.create("https://problems.affina.com.vn/http-message-not-readable-error"))
                    .title("HTTP Message Not Readable Error")
                    .detail(e.getMessage())
                    .instance(URI.create(request.getRequestURI()))
                    .code("HTTP_MESSAGE_NOT_READABLE_ERROR")
                    .build();
        } else if (exception instanceof HttpMessageNotWritableException e) {
            return RestError.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .type(URI.create("https://problems.affina.com.vn/http-message-not-writable-error"))
                    .title("HTTP Message Not Writable Error")
                    .detail(e.getMessage())
                    .instance(URI.create(request.getRequestURI()))
                    .code("HTTP_MESSAGE_NOT_WRITABLE_ERROR")
                    .build();
        } else if (exception instanceof ErrorResponse e) {
            return RestError.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .type(URI.create("https://problems.affina.com.vn/common-request-error"))
                    .title("Common Request Error")
                    .detail("(%s) %s :: %s".formatted(
                            ((HttpStatus) e.getStatusCode()).name(),
                            e.getBody().getTitle(),
                            e.getBody().getDetail()))
                    .instance(URI.create(request.getRequestURI()))
                    .code("COMMON_REQUEST_ERROR")
                    .build();
        } else {
            throw new IllegalStateException("Unexpected value: " + exception);
        }
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    default RestError handleDataIntegrityViolationException(DataIntegrityViolationException e,
                                                            HttpServletRequest request) {
        log.error("Stack trace of Data Integrity Violation Error", e);
        return RestError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .type(URI.create("https://problems.affina.com.vn/data-integrity-violation"))
                .title("Data Integrity Violation")
                .detail(NestedExceptionUtils.getMostSpecificCause(e).getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("DATA_INTEGRITY_VIOLATION")
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    default RestError handleGlobalException(Exception e, HttpServletRequest request) {
        log.error("Stack trace of Internal Server Error", e);
        return RestError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .type(URI.create("https://problems.affina.com.vn/internal-server-error"))
                .title("Internal Server Error")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code("INTERNAL_SERVER_ERROR")
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    default ResponseEntity<RestError> handleNotFoundException(NotFoundException e, HttpServletRequest request) {
        RestError restError = RestError.builder()
                .status(e.getHttpStatus())
                .type(URI.create("https://problems.affina.com.vn/not-found"))
                .title("Not Found")
                .detail(e.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .code(e.getCode())
                .build();
        return ResponseEntity.status(e.getHttpStatus()).body(restError);
    }

    @ExceptionHandler(CompletionException.class)
    public default ResponseEntity<RestError> handleCompletionException(CompletionException ex) {
        RestError restError = RestError.builder()
                .status(222)
                .type(URI.create("https://problems.affina.com.vn/not-found"))
                .title("Not Found")
                .detail("222")
                .code("222")
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(restError);
    }
}
