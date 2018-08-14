package com.demo.util;

import java.util.Date;

public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String details;
    private int  status;


    public ErrorDetails(Date timestamp, String message, String details,int code) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.status=code;
    }

    public int getStatus(){
        return status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

}