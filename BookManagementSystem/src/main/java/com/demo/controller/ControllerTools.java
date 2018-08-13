package com.demo.controller;

import com.demo.exception.IdError;
import com.demo.exception.IsbnNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.regex.Pattern;

@ControllerAdvice
public class ControllerTools {

    @Value("${datasource}")
    String datasource;

    @Autowired
    private ApplicationContext applicationContext;

    public String getDatasource() {
        return datasource;
    }
    public ApplicationContext getApplicationContext(){return applicationContext;}

    public String isVaildIsbn(String isbn) throws IsbnNotFound {
        if (isbn==null) return null;
        String regex = "[^A-Za-z0-9]";
        if (!Pattern.compile(regex).matcher(isbn).find()){
            if(isbn.length()==13){
                if (isbn.startsWith("978")){
                    return isbn;
                }else{
                    throw new IsbnNotFound("isbn: "+isbn+" is not vaild");
                }
            }
            if (isbn.length()==10){
                return "978"+isbn;
            }
        }
        throw new IsbnNotFound("isbn: "+isbn+" is not vaild");
    }

    public String isVaildId(String id ) throws IdError {
        if (id==null) return null;
        String regex = "[^A-Za-z0-9-]";
        if(Pattern.compile(regex).matcher(id).find()){
            throw new IdError("id: "+id+" is not a vaild id");
        }else{
            return id;
        }
    }

}
