package com.demo.util;
import com.demo.exception.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class Handler extends ResponseEntityExceptionHandler {

    private void printLog(Exception ex, WebRequest request){
        System.out.println("====================================================================");
        System.out.println(ex.getMessage());
        System.out.println(request.getDescription(false));
        System.out.println("====================================================================");
    }

    @ExceptionHandler(IsbnNotFound.class)
    public final ResponseEntity<ErrorDetails> handleIsbnNotFoundException(IsbnNotFound ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false),500);
        printLog(ex,request);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(BooknameNotFound.class)
    public final ResponseEntity<ErrorDetails> handleBooknameNotFoundException(BooknameNotFound ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false),500);
        printLog(ex,request);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(DatabaseError.class)
    public final ResponseEntity<ErrorDetails> handleDatabaseNotFoundException(DatabaseError ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false),500);
        printLog(ex,request);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(PageNotFound.class)
    public final ResponseEntity<ErrorDetails> handleDatabaseNotFoundException(PageNotFound ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false),404);
        printLog(ex,request);
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}


