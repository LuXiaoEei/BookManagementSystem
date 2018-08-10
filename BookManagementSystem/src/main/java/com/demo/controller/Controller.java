package com.demo.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import com.demo.exception.*;
import com.demo.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.*;

@RestController

public class Controller{

    @Value("${datasource}")
    private String datasource;

//    private String a=datasource;

    @Autowired
    @Qualifier("serviceMysql")
    Service service;


    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private String isVaildIsbn(String isbn) throws  IsbnNotFound {
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

    private String isVaildId(String id ) throws IdError {
        if (id==null) return null;
        String regex = "[^0-9]";
        if(Pattern.compile(regex).matcher(id).find()){
            throw new IdError("id: "+id+" is not a vaild id");
        }else{
            return id;
        }
    }


    @RequestMapping(value = {"/**","/*"},method = RequestMethod.GET)
    public void noPageFind(HttpServletRequest request,HttpServletResponse response) throws IOException, PageNotFound {
        System.out.println("exception: no page found: "+ request.getRequestURI()+" is not a vaild url!");
        throw new PageNotFound("Error: invalid url: "+request.getRequestURI()+" ; Page not found!");
    }



    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({PageNotFound.class,IsbnNotFound.class,IdError.class,DatabaseError.class,BooknameNotFound.class,MissingServletRequestParameterException.class, NoSuchFieldException.class})
    public final ResponseEntity<ErrorDetails> handleUserNotFoundException(Exception ex, WebRequest request) {
        System.out.println("======================================================");
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false));
        System.out.println(ex.getMessage());
        System.out.println("======================================================");
        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public Object addBook(@RequestParam(value = "id", required = false,defaultValue = "") String id,
                          @RequestParam(value = "isbn", required = true) String isbn,
                          @RequestParam(value = "press", required = false,defaultValue = "") String press,
                          @RequestParam(value = "user", required = false,defaultValue = "") String user,
                          @RequestParam(value = "loantime", required = false,defaultValue = "") String loantime,
                          @RequestParam(value = "bookname", required = true) String bookname,
                          @RequestParam(value = "category", required = false,defaultValue = "") String category,
                          @RequestParam(value = "returntime", required = false,defaultValue = "") String returntime,
                          HttpServletResponse response) throws IOException, IdError, IsbnNotFound {
        return service.addbook(isVaildId(id),bookname,isVaildIsbn(isbn),category,press,user,loantime,returntime,df.format(new Date()),response);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object deleteBook(@RequestParam(value = "isbn", required = true) String isbn,
                             HttpServletResponse response) throws IsbnNotFound, IOException {
        return service.deleteBookByIsbn(isVaildIsbn(isbn),response);
    }


    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public Object updateBook(@RequestParam(value = "press", required = false) String press,
                             @RequestParam(value = "category", required = false,defaultValue = "") String category,
                             @RequestParam(value = "bookname", required = false) String bookname,
                             @RequestParam(value = "isbn", required = false) String isbn,
                             @RequestParam(value = "condition", required = true) String condition,
                             HttpServletResponse response) throws IsbnNotFound, BooknameNotFound, IOException {
        return service.updateBookByIsbn(press,category,bookname,isVaildIsbn(isbn),isVaildIsbn(condition),response);
    }

    @RequestMapping(value = "/describe", method = RequestMethod.GET)
    public  Object describeBook(
            @RequestParam(value = "start", required = false,defaultValue = "0") String start,
            @RequestParam(value = "nums", required = false,defaultValue = "all") String nums,
            HttpServletResponse response) throws IOException {
        return service.describeBook(start,nums,response);
    }

    @RequestMapping(value = "/select/bookname/{bookname}", method = RequestMethod.GET)
    public Object findBook(@PathVariable(required = true) String bookname,
                           HttpServletResponse response) throws IOException {
        return service.selectByBooknameLike(bookname,response);
    }

    @RequestMapping(value = "/select/user/{user}", method = RequestMethod.GET)
    public Object findUser(@PathVariable(required = true) String user,
                           HttpServletResponse response) throws IOException {
        return service.selectByUser(user,response);
    }

    @RequestMapping(value = "/loanbook", method = RequestMethod.GET)
    public void loanBook(@RequestParam(value = "isbn", required = true) String isbn,
                         @RequestParam(value = "user", required = true) String user,
                         HttpServletResponse response) throws IOException, IsbnNotFound, ParseException {
        service.loanBookByUserAndIsbn(user,isVaildIsbn(isbn),response);
    }

    @RequestMapping(value = "/returnbook", method = RequestMethod.GET)
    public Object returnBook(@RequestParam(value = "isbn", required = true) String isbn,
                             @RequestParam(value = "user", required = true) String user,
                             HttpServletResponse response) throws IOException, IsbnNotFound, ParseException {
        return service.returnbookByUserAndIsbn(user,isVaildIsbn(isbn),response);

    }






















}