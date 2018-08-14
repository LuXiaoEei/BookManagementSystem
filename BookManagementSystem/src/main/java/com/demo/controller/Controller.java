package com.demo.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.demo.error.*;
import com.demo.service.Service;
import com.demo.service.ServiceFile;
import com.demo.service.ServiceGemfire;
import com.demo.service.ServiceMysql;
import com.demo.util.ControllerTools;
import com.demo.util.ErrorDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.*;

@RestController

public class Controller{

    private String datasource;
    private  Service service;
    private ControllerTools controllerTools;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


    @Autowired
   public Controller(ControllerTools controllerTools){
        this.datasource= controllerTools.getDatasource();
        this.controllerTools=controllerTools;
        switch (controllerTools.getDatasource()){
            case "File":
                this.service = controllerTools.getApplicationContext().getBean(ServiceFile.class);
                break;
            case "Gemfire":
                this.service = controllerTools.getApplicationContext().getBean(ServiceGemfire.class);
                break;
            case "Mysql":
                this.service = controllerTools.getApplicationContext().getBean(ServiceMysql.class);
                break;
            default:
                throw new DatabaseSelectException("You choose the "+ controllerTools.getDatasource()+"; Please choose the vaild datasource. It supports File/Gemfire/Mysql.");
        }
    }

    @RequestMapping(value = {"/**","/*"},method = RequestMethod.GET)
    public void noPageFind(HttpServletRequest request,HttpServletResponse response) throws IOException, PageNotFoundException {
        throw new PageNotFoundException("Error: invalid url: "+request.getRequestURI()+" ; Page not found!");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public final ResponseEntity<ErrorDetails> handleUserNotFoundException(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false),500);
        System.out.println("====================================================================");
        System.out.println(ex.getMessage());
        System.out.println(request.getDescription(false));
        System.out.println("====================================================================");
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public void getInfo(HttpServletResponse response) throws IOException {
        response.getWriter().println("The datasource used now is "+datasource.replace("service","")+";");
        response.getWriter().println("<br/>");
        response.getWriter().println("The amount of data in the database is "+ service.countAll()+";");
        response.flushBuffer();
    }

    @RequestMapping(value = "/add", method = {RequestMethod.POST, RequestMethod.GET})
    public Object addBook(@RequestParam(value = "isbn", required = true) String isbn,
                          @RequestParam(value = "press", required = false,defaultValue = "") String press,
                          @RequestParam(value = "user", required = false,defaultValue = "") String user,
                          @RequestParam(value = "loantime", required = false,defaultValue = "") String loantime,
                          @RequestParam(value = "bookname", required = true) String bookname,
                          @RequestParam(value = "category", required = false,defaultValue = "") String category,
                          @RequestParam(value = "returntime", required = false,defaultValue = "") String returntime,
                          HttpServletResponse response) throws IOException, IsbnNotFoundException {
        return service.addbook(controllerTools.IdAutoGeneration(),bookname,controllerTools.isVaildIsbn(isbn),category,press,user,loantime,returntime,df.format(new Date()),response);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object deleteBook(@RequestParam(value = "isbn", required = true) String isbn,
                             HttpServletResponse response) throws IsbnNotFoundException, IOException {
        return service.deleteBookByIsbn(controllerTools.isVaildIsbn(isbn),response);
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public Object updateBook(@RequestParam(value = "press", required = false) String press,
                             @RequestParam(value = "category", required = false,defaultValue = "") String category,
                             @RequestParam(value = "bookname", required = false) String bookname,
                             @RequestParam(value = "isbn", required = false) String isbn,
                             @RequestParam(value = "condition", required = true) String condition,
                             HttpServletResponse response) throws IsbnNotFoundException, BooknameNotFoundException, IOException {
        return service.updateBookByIsbn(press,category,bookname,controllerTools.isVaildIsbn(isbn),controllerTools.isVaildIsbn(condition),response);
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
                           HttpServletResponse response) throws IOException, ParseException {
        return service.selectByUser(user,response);
    }

    @RequestMapping(value = "/loanbook", method = RequestMethod.GET)
    public void loanBook(@RequestParam(value = "isbn", required = true) String isbn,
                         @RequestParam(value = "user", required = true) String user,
                         HttpServletResponse response) throws IOException, IsbnNotFoundException, ParseException {
        service.loanBookByUserAndIsbn(user,controllerTools.isVaildIsbn(isbn),response);
    }

    @RequestMapping(value = "/returnbook", method = RequestMethod.GET)
    public Object returnBook(@RequestParam(value = "isbn", required = true) String isbn,
                             @RequestParam(value = "user", required = true) String user,
                             HttpServletResponse response) throws IOException, IsbnNotFoundException, ParseException {
        return service.returnbookByUserAndIsbn(user,controllerTools.isVaildIsbn(isbn),response);

    }
}