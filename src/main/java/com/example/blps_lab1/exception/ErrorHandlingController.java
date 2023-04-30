package com.example.blps_lab1.exception;

import com.example.blps_lab1.dto.response.ErrorResponse;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ErrorHandlingController {

    @ExceptionHandler({PermissionDeniedException.class,
            TokenHasExpiredException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            IllegalPageParametersException.class,
            BadCredentialsException.class,
            ResourceAlreadyExistException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(RuntimeException e) {
        return new ErrorResponse(e.getMessage());

    }

    @ExceptionHandler({UsernameNotFoundException.class,
            ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

}
