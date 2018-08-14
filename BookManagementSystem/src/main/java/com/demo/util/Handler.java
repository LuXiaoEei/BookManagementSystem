package com.demo.util;
import com.demo.error.*;

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

    @ExceptionHandler(IsbnNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleIsbnNotFoundException(IsbnNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false),500);
        printLog(ex,request);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(BooknameNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleBooknameNotFoundException(BooknameNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false),500);
        printLog(ex,request);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(DatabaseSelectException.class)
    public final ResponseEntity<ErrorDetails> handleDatabaseNotFoundException(DatabaseSelectException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false),500);
        printLog(ex,request);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(PageNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleDatabaseNotFoundException(PageNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false),404);
        printLog(ex,request);
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}


