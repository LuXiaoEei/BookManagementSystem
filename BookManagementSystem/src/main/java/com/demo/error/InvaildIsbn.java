package com.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InvaildIsbn extends Exception{
    public InvaildIsbn(String exception) {
        super(exception);
    }
}
