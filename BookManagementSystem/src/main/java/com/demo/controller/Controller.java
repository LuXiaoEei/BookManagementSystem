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
    /**
     * 这个方法的相关需修改在会上说过了这里不再赘述 另外上面的@Autowired在这是几个意思
     */
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
    /**
     * 这个可以建一个拦截器类来做拦截
     * 异常的处理：一个友好的服务端通常会捕获自己的异常并做封装返回相应错误码及错误信息而不是直接把异常抛给客户端
     */
    public void noPageFind(HttpServletRequest request,HttpServletResponse response) throws IOException, PageNotFoundException {
        throw new PageNotFoundException("Error: invalid url: "+request.getRequestURI()+" ; Page not found!");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    /**
     * 与业务逻辑无关的方法单独放在一个类里面（一些可以抽象出来的，通用的方法）
     * 日志不要用System.out.println打印，并且打印时可以带上参数等信息，打印日志的原则之一是当你看到这行日志时可以迅速定位到该方法
     */
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
    /**
     * 一次完整的请求返回通常包括响应码和响应信息
     * 响应码是你与客户端约定好的一系列代码，这样客户端可以通过识别响应码来做相应处理;如用404代码请求地址不存在，500代码服务器异常，等等
     * 真正进行业务逻辑实现时响应码会有很多种，这只是一种约定，如果服务端与客户端约定了6666代表成功，那6666就是成功
     * 响应信息通常包括成功或失败标志以及结果数据
     */
    public void getInfo(HttpServletResponse response) throws IOException {
        response.getWriter().println("The datasource used now is "+datasource.replace("service","")+";");
        response.getWriter().println("<br/>");
        response.getWriter().println("The amount of data in the database is "+ service.countAll()+";");
        response.flushBuffer();
    }

    @RequestMapping(value = "/add", method = {RequestMethod.POST, RequestMethod.GET})
    /**
     * todo  字段名字采用驼峰命名法，如：returnTime
     * 一个方法参数过多时考虑用对象（model）
     * todo  为什么每个方法的参数都带着HttpServletResponse
     */
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
    /**
     * todo 请求方法除了GET，还有POST PUT DELETE等等
     */
    public Object deleteBook(@RequestParam(value = "isbn", required = true) String isbn,
                             HttpServletResponse response) throws IsbnNotFoundException, IOException {
        return service.deleteBookByIsbn(controllerTools.isVaildIsbn(isbn),response);
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    /**
     * POST请求方式时，参数可以通过请求体接收，这里可以写成updateBook(@RequestBody Book book)这种形式
     */
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