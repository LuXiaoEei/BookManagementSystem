package com.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DatabaseError extends Exception{
    public DatabaseError(String exception) {
        super(exception);
    }
}