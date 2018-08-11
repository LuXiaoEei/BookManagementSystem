package com.demo.controller;
import com.demo.exception.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class Handler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(IsbnNotFound.class)
    public final ResponseEntity<ErrorDetails> handleIsbnNotFoundException(IsbnNotFound ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(true),500);
        ResponseEntity<ErrorDetails> temp=new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
        System.out.println(temp.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(BooknameNotFound.class)
    public final ResponseEntity<ErrorDetails> handleBooknameNotFoundException(BooknameNotFound ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false),500);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(DatabaseError.class)
    public final ResponseEntity<ErrorDetails> handleDatabaseNotFoundException(DatabaseError ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false),500);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(PageNotFound.class)
    public final ResponseEntity<ErrorDetails> handleDatabaseNotFoundException(PageNotFound ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false),500);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(IdError.class)
    public final ResponseEntity<ErrorDetails> handleIderrorExceptions(IdError ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false),500);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

//class  MyErrorController extends BasicErrorController {
//
//
//    public MyErrorController(){
//        super(new DefaultErrorAttributes(), new ErrorProperties());
//    }


