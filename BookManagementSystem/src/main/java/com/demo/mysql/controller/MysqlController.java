package com.demo.mysql.controller;


import com.demo.mysql.model.BookMysql;
import com.demo.mysql.repository.BookRepositoryMysql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addBook(@RequestParam(value = "isbn", required = true) String isbn,
                            @RequestParam(value = "bookname", required = true) String bookname,
                            @RequestParam(value = "press", required = false,defaultValue = "") String press,
                            @RequestParam(value = "category", required = false,defaultValue = "") String category) {
            BookMysql p = new BookMysql(isbn,bookname,press,category,new Date(),"","","");
            bookRepositoryMysql.save(p);
        return p.toString();
    }

    @RequestMapping(value = "/describle", method = RequestMethod.GET)
    public  Collection<BookMysql> describleBook(
//            @RequestParam(value = "start", required = false,defaultValue = "5") String start,
            @RequestParam(value = "end", required = false,defaultValue = "all") String end){
//        int starts = Integer.valueOf(start);
        int ends = (end.equals("all"))?bookRepositoryMysql.countAll():Integer.valueOf(end);
        return bookRepositoryMysql.describle(ends);
    }

    @RequestMapping(value="/lendbook", method = RequestMethod.GET)
    public void  lendBook(@RequestParam(value = "bookname", required = true) String bookname,
                          @RequestParam(value = "user", required = true) String user,
                          HttpServletResponse response){
        Collection<BookMysql> res = bookRepositoryMysql.serachNolendedBookByBookname(bookname);
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
                response.getWriter().println(user+" success borrowing "+ bookname +" at "+df.format(new Date()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value="/returnbook", method = RequestMethod.GET)
    public void  returnBook(@RequestParam(value = "bookname", required = true) String bookname,
                          @RequestParam(value = "user", required = true) String user,
                          HttpServletResponse response){
        Collection<BookMysql> res = bookRepositoryMysql.serachlendedBookByBooknameAndUser(user,bookname);
        if (res.isEmpty()){
            try {
                response.getWriter().println("you have not borrowed the book");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Long id =res.iterator().next().getId();
            bookRepositoryMysql.returnBook(id);
            try {
                response.getWriter().println(user+" success returning "+ bookname +" at "+ df.format(new Date()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @RequestMapping(value="/select", method = RequestMethod.GET)
    public Collection<BookMysql> selectById(@RequestParam(value = "id", required = true) Long id){
        return bookRepositoryMysql.selectById(id);
    }
}