package com.demo.geode.controller;

import com.demo.geode.model.BookGemfire;
import com.demo.geode.repository.BookRepositoryGemfire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

@RestController
@RequestMapping(value = "/gemfire/book/*" ,method = RequestMethod.GET)
public class GemfireController {

    @Autowired
    private BookRepositoryGemfire bookRepositoryGemfire;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addBook(@RequestParam(value = "isbn", required = true) String isbn,
                            @RequestParam(value = "bookname", required = true) String bookname,
                            @RequestParam(value = "category", required = false,defaultValue = "") String category,
                            @RequestParam(value = "publicationyear", required = false,defaultValue = "") String publicationyear) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        BookGemfire p = new BookGemfire(isbn,bookname,category,publicationyear,df.format(new Date()));
        bookRepositoryGemfire.save(p);
        return p.toString();
    }

    @RequestMapping(value = "/describle", method = RequestMethod.GET)
    public Collection<BookGemfire> describleBook(@RequestParam(value = "rows", required = false,defaultValue = "-1") int rows){
//        int row = (rows.equals("all"))?bookRepositoryGemfire.countAll():Integer.valueOf(rows);
        return bookRepositoryGemfire.describle(rows);
    }
}
