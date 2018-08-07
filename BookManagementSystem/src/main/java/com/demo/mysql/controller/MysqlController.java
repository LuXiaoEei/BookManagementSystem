package com.demo.mysql.controller;


import com.demo.mysql.model.BookMysql;
import com.demo.mysql.repository.BookRepositoryMysql;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

@RestController
@RequestMapping(value = "/2" ,method = RequestMethod.GET)
public class MysqlController {
    @Autowired
    private BookRepositoryMysql bookRepositoryMysql;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void processMethod(Exception ex, HttpServletRequest request , HttpServletResponse response) throws IOException {
        System.out.println("error: "+ex.getMessage());
        response.getWriter().printf("error: "+ex.getMessage());
        response.flushBuffer();
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addBook(@RequestParam(value = "isbn", required = true) String isbn,
                            @RequestParam(value = "bookname", required = true) String bookname,
                            @RequestParam(value = "press", required = false,defaultValue = "") String press,
                            @RequestParam(value = "category", required = false,defaultValue = "") String category) {
            BookMysql p = new BookMysql(isbn,bookname,press,category,new Date(),"","","");
            bookRepositoryMysql.save(p);
        return p.toString();
    }

    @RequestMapping(value = "/describe", method = RequestMethod.GET)
    public  Collection<BookMysql> describeBook(
            @RequestParam(value = "start", required = false,defaultValue = "0") String start,
            @RequestParam(value = "nums", required = false,defaultValue = "all") String nums){
        int starts = Integer.valueOf(start);
        int num = (nums.equals("all"))?bookRepositoryMysql.countAll():Integer.valueOf(nums);
        return bookRepositoryMysql.describe(starts, num);
    }

    @RequestMapping(value="/loanbook", method = RequestMethod.GET)
    public void  loanBook(@RequestParam(value = "isbn", required = true) String isbn,
                          @RequestParam(value = "user", required = true) String user,
                          HttpServletResponse response){
        Collection<BookMysql> res = bookRepositoryMysql.serachNoloanedBookByIsbn(isbn);
        if (res.isEmpty()){
            try {
                response.getWriter().println("no book remained");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Long id =res.iterator().next().getId();
            bookRepositoryMysql.lendBook(user,df.format(new Date()),id);
            try {
                response.getWriter().println(user+" success borrowing isbn: s"+ isbn +" at "+df.format(new Date()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value="/returnbook", method = RequestMethod.GET)
    public void  returnBook(@RequestParam(value = "isbn", required = true) String isbn,
                          @RequestParam(value = "user", required = true) String user,
                          HttpServletResponse response) throws IOException {
        Collection<BookMysql> res = bookRepositoryMysql.serachloanedBookByBooknameAndIsbn(user,isbn);
        if (res.isEmpty()){
            response.getWriter().println("you have not borrowed the book");
        }else{
            Long id =res.iterator().next().getId();
            bookRepositoryMysql.returnBook(id);
            response.getWriter().println(user+" success returning isbn: "+ isbn +" at "+ df.format(new Date()));
            }
        }

    @RequestMapping(value="/update", method = RequestMethod.GET)
    public Object update(@RequestParam(value = "bookname", required = false) String bookname,
                                        @RequestParam(value = "category", required = false,defaultValue = "") String category,
                                        @RequestParam(value = "press", required = false) String press,
                                        @RequestParam(value = "isbn", required = false) String isbn,
                                        @RequestParam(value = "condition", required = true) String condition,
                                        HttpServletResponse response) throws IOException {
        String datetime = df.format(new Date());
        if (!StringUtils.isBlank(isbn) && ((isbn.equals("''")|(isbn.equals("\"\""))))){
            response.getWriter().println("don't set the isbn to null");
            return null;
        }else {
            if (!StringUtils.isBlank(bookname)) bookRepositoryMysql.updateBookname((bookname.equals("''") | (bookname.equals("\"\""))) ? "" : bookname, datetime, condition);
            if (!StringUtils.isBlank(category)) bookRepositoryMysql.updateCategory((category.equals("''") | (category.equals("\"\""))) ? "" : category, datetime, condition);
            if (!StringUtils.isBlank(press))bookRepositoryMysql.updatePress((press.equals("''") | (press.equals("\"\""))) ? "" : press, datetime, condition);
            if (!StringUtils.isBlank(isbn)) bookRepositoryMysql.updateIsbn(isbn, datetime, condition);

            String tmpisbn = (StringUtils.isBlank(isbn)) ? condition : isbn;
            return bookRepositoryMysql.selectByIsbn(tmpisbn);
        }
    }

    @RequestMapping(value="/select/bookname/{bookname}", method = RequestMethod.GET)
    public Collection<BookMysql> findByBooknameContains(@PathVariable(required = true) String bookname){
        return bookRepositoryMysql.findByBooknameContains(bookname);
    }

    @RequestMapping(value="/select/user/{user}", method = RequestMethod.GET)
    public Collection<BookMysql> findByUserEquals(@PathVariable(required = true) String user){
        return bookRepositoryMysql.findByUserEquals(user);
    }

    @RequestMapping(value="/delete", method = RequestMethod.GET)
    public Collection<BookMysql> deleteByIsbnEquals(@RequestParam(value = "isbn", required = true) String isbn){
        return bookRepositoryMysql.deleteByIsbnEquals(isbn);
    }

}