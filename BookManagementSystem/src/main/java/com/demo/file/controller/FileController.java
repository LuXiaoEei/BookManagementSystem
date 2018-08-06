package com.demo.file.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

@RestController
@RequestMapping(value = "/file/book/*" ,method = RequestMethod.GET)
public class FileController {

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addBook(@RequestParam(value = "isbn", required = true) String isbn,
                            @RequestParam(value = "bookname", required = true) String bookname,
                            @RequestParam(value = "category", required = false,defaultValue = "") String category,
                            @RequestParam(value = "publicationyear", required = false,defaultValue = "") String publicationyear) {
        return"1";

    }

    @RequestMapping(value = "/describle", method = RequestMethod.GET)
    public String describleBook(@RequestParam(value = "rows", required = false,defaultValue = "-1") int rows){
        return "1";
    }
}

