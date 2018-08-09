package com.demo.error;

        import org.springframework.http.HttpStatus;
        import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class IsbnNotFound extends Exception{
    public IsbnNotFound(String exception) {
        super(exception);
    }
}
