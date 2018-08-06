package com.demo.mysql.controller;


import com.demo.mysql.model.BookMysql;
import com.demo.mysql.repository.BookRepositoryMysql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;
import java.util.Date;

@RestController
@RequestMapping(value = "/mysql/book" ,method = RequestMethod.GET)
public class MysqlController {
    @Autowired
    private BookRepositoryMysql bookRepositoryMysql;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addBook(@RequestParam(value = "isbn", required = true) String isbn,
                            @RequestParam(value = "bookname", required = true) String bookname,
                            @RequestParam(value = "category", required = false,defaultValue = "") String category,
                            @RequestParam(value = "publicationyear", required = false,defaultValue = "") String publicationyear) {
        BookMysql p = new BookMysql(isbn,bookname,category,publicationyear,new Date());
        bookRepositoryMysql.save(p);
        return p.toString();
    }

    @RequestMapping(value = "/describle", method = RequestMethod.GET)
    public  Collection<BookMysql> describleBook(@RequestParam(value = "rows", required = false,defaultValue = "all") String rows){
        int row = (rows.equals("all"))?bookRepositoryMysql.countAll():Integer.valueOf(rows);
        return bookRepositoryMysql.describle(row);
    }
}