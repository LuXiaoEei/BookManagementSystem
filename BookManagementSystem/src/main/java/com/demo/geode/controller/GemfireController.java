package com.demo.geode.controller;

import com.demo.geode.model.BookGemfire;
import com.demo.geode.repository.BookRepositoryGemfire;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

@RestController
@RequestMapping(value = "/1" ,method = RequestMethod.GET)
public class GemfireController {

    @Autowired
    private BookRepositoryGemfire bookRepositoryGemfire;
    @ExceptionHandler(Exception.class)
    public void processMethod(Exception ex, HttpServletRequest request , HttpServletResponse response) throws IOException {
        System.out.println("error: "+ex.getMessage());
        response.getWriter().printf("error: "+ex.getMessage());
        response.flushBuffer();
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addBook(@RequestParam(value = "id", required = true) String id,
                            @RequestParam(value = "isbn", required = true) String isbn,
                            @RequestParam(value = "press", required = false,defaultValue = "") String press,
                            @RequestParam(value = "user", required = false,defaultValue = "") String user,
                            @RequestParam(value = "loantime", required = false,defaultValue = "") String loantime,
                            @RequestParam(value = "bookname", required = true) String bookname,
                            @RequestParam(value = "category", required = false,defaultValue = "") String category,
                            @RequestParam(value = "returntime", required = false,defaultValue = "") String returntime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        BookGemfire p = new BookGemfire(id,bookname,isbn,category,press,user,loantime,returntime,df.format(new Date()));
        bookRepositoryGemfire.save(p);
        return p.toString();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Collection<BookGemfire> deleteBook(@RequestParam(value = "isbn", required = true) String isbn){
        Collection<BookGemfire> result =bookRepositoryGemfire.findByIsbn(isbn);
        bookRepositoryGemfire.deleteById(result.iterator().next().getId());
        return result;
    }

    @RequestMapping(value = "/select/bookname/{bookname}", method = RequestMethod.GET)
    public Collection<BookGemfire> findBook(@PathVariable(required = true) String bookname) {
        Collection<BookGemfire> result = bookRepositoryGemfire.findByBooknameLike("%"+bookname+"%");
        return result;
    }

    @RequestMapping(value = "/select/user/{user}", method = RequestMethod.GET)
    public Collection<BookGemfire> findUser(@PathVariable(required = true) String user) {
        Collection<BookGemfire> result = bookRepositoryGemfire.findByUser(user);
        return result;
    }

    @RequestMapping(value = "/describe/{num}", method = RequestMethod.GET)
    public Collection<BookGemfire> describe(@PathVariable(required = false) int num) {
        Collection<BookGemfire> result = bookRepositoryGemfire.describle(num);
        return result;
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public Collection<BookGemfire> updateBook(@RequestParam(value = "press", required = false) String press,
                                              @RequestParam(value = "category", required = false,defaultValue = "") String category,
                                              @RequestParam(value = "bookname", required = false) String bookname,
                                              @RequestParam(value = "isbn", required = true) String isbn) {
        Collection<BookGemfire> result=bookRepositoryGemfire.findByIsbn(isbn);
        if(!StringUtils.isBlank(bookname))
            result.iterator().next().setBookname(bookname);
        if(!StringUtils.isBlank(press))
            result.iterator().next().setPress(press);
        if (!StringUtils.isBlank(category))
            result.iterator().next().setCategory(category);
        bookRepositoryGemfire.save(result.iterator().next());
        return result;
    }

    @RequestMapping(value = "/loan", method = RequestMethod.GET)
    public String loanBook(@RequestParam(value = "isbn", required = true) String isbn,
                           @RequestParam(value = "user", required = true) String user) throws ParseException {
        Collection<BookGemfire> result =bookRepositoryGemfire.findByIsbn(isbn);
        BookGemfire temp=result.iterator().next();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if (temp.getLoantime().equals("")){
            temp.setLoantime(df.format(new Date()));
            temp.setUser(user);
            bookRepositoryGemfire.save(temp);
            return result.toString();
        }
        else if (!temp.getReturntime().equals("")&&df.parse(temp.getReturntime()).after(df.parse(temp.getLoantime()))){
            temp.setLoantime(df.format(new Date()));
            temp.setUser(user);
            bookRepositoryGemfire.save(temp);
            return result.toString();
        }else
            return "Sorry, the book has been loaned";
    }

    @RequestMapping(value = "/return", method = RequestMethod.GET)
    public Collection<BookGemfire> returnBook(@RequestParam(value = "isbn", required = true) String isbn,
                                              @RequestParam(value = "user", required = true) String user) throws ParseException {
        Collection<BookGemfire> result =bookRepositoryGemfire.findByIsbn(isbn);
        BookGemfire temp=result.iterator().next();
        temp.setUser(user);
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        result.iterator().next().setReturntime(df.format(new Date()));
        return result;
    }
}
