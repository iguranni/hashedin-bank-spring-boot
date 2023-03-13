package com.hashedin.hashedinbank.exception;

import com.hashedin.hashedinbank.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public final ResponseEntity<ApiResponse<String>> handleAllException(Exception exception) {
        return new ResponseEntity<>(new ApiResponse<>(exception.getMessage(), null, null), HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(value = CompanyAlreadyExistsException.class)
    public final ResponseEntity<ApiResponse<String>> handleCompanyAlreadyExistsException(CompanyAlreadyExistsException exception) {
        return new ResponseEntity<>(new ApiResponse<>(exception.getMessage(), null, null), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = CompanyNotFoundException.class)
    public final ResponseEntity<ApiResponse<String>> handleCompanyNotFoundExceptionException(CompanyNotFoundException exception) {
        return new ResponseEntity<>(new ApiResponse<>(exception.getMessage(), null, null), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = RoleNotFoundException.class)
    public final ResponseEntity<ApiResponse<String>> handleRoleNotFoundExceptionException(RoleNotFoundException exception) {
        return new ResponseEntity<>(new ApiResponse<>(exception.getMessage(), null, null), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public final ResponseEntity<ApiResponse<String>> handleUserAlreadyExistsExceptionException(UserAlreadyExistsException exception) {
        return new ResponseEntity<>(new ApiResponse<>(exception.getMessage(), null, null), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public final ResponseEntity<ApiResponse<String>> handleUserNotFoundExceptionException(UserNotFoundException exception) {
        return new ResponseEntity<>(new ApiResponse<>(exception.getMessage(), null, null), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ExpenseCategoryNotFoundException.class)
    public final ResponseEntity<ApiResponse<String>> handleExpenseCategoryNotFoundException(ExpenseCategoryNotFoundException exception) {
        return new ResponseEntity<>(new ApiResponse<>(exception.getMessage(), null, null), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DataTransformationException.class)
    public final ResponseEntity<ApiResponse<String>> handleDataTransformationException(DataTransformationException exception) {
        return new ResponseEntity<>(new ApiResponse<>(exception.getMessage(), null, null), HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public final ResponseEntity<ApiResponse<String>> handleBadCredentialsException(BadCredentialsException exception) {
        return new ResponseEntity<>(new ApiResponse<>(exception.getMessage(), null, null), HttpStatus.EXPECTATION_FAILED);
    }
}