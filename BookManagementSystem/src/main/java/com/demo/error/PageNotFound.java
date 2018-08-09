package com.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PageNotFound extends Exception {
    public PageNotFound(String exception) {
        super(exception);
    }

}


