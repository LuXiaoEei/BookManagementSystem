package com.demo.controller;

import java.io.IOException;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.*;

@RestController
public class Controller {

//    private static final String template = "Hello, %s!";
//    private final AtomicLong counter = new AtomicLong();s

    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void processMethod(Exception ex,HttpServletRequest request ,HttpServletResponse response) throws IOException {
        System.out.println("抛异常了！"+ex.getLocalizedMessage());
//        logger.error("抛异常了！"+ex.getLocalizedMessage());
        response.getWriter().printf("error: "+ex.getMessage());
        response.flushBuffer();
    }


}