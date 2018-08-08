package com.demo.file.controller;


import com.demo.file.model.BookFile;
import com.demo.file.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import  java.io.*;
import java.lang.System;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/0" ,method = RequestMethod.GET)
public class FileController {
    @Autowired
    FileRepository fileRepository;

//    FileManager fileManager = new FileManager();
    private Path rootpath = Paths.get("", "src","main","java","com","demo","file","date");
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @ExceptionHandler(Exception.class)
    public void processMethod(Exception ex, HttpServletRequest request , HttpServletResponse response) throws IOException {
        System.out.println("error: "+ex.getMessage());
        response.getWriter().printf("error: "+ex.getMessage());
        response.flushBuffer();
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public BookFile addBook(@RequestParam(value = "id", required = true) String id,
                        @RequestParam(value = "isbn", required = true) String isbn,
                        @RequestParam(value = "bookname", required = true) String bookname,
                        @RequestParam(value = "press", required = false,defaultValue = "") String press,
                        @RequestParam(value = "category", required = false,defaultValue = "") String category,
                        HttpServletResponse response) throws IOException {

        BookFile bookFile = new BookFile(id,isbn,bookname,category,press,df.format(new Date()),"","","");
        return fileRepository.save(bookFile);
    }

    @RequestMapping(value = "/describe", method = RequestMethod.GET)
    public ArrayList<BookFile> describe(@RequestParam(value = "start", required = false,defaultValue = "0") int start,
                                        @RequestParam(value = "nums", required = false,defaultValue = "all") String nums
    ) throws IOException {
        HashMap<Integer,BookFile> allcontent = fileRepository.readFileSource();
        ArrayList<BookFile> result = new ArrayList<BookFile>();
        Integer end=((nums.equals("all"))?allcontent.keySet().size():Integer.valueOf(nums))+start;
        for (Integer i=start;i<end;i++){
            if (allcontent.get(i)!=null){
                result.add(allcontent.get(i));
            }
        }
        return result;
    }

    @RequestMapping(value = "/select/user/{user}", method = RequestMethod.GET)
    public ArrayList<BookFile> findByUser(@PathVariable(required = true) String user) throws IOException {
        Collection<BookFile> allcontent = fileRepository.readFileSource().values();
        return allcontent.stream().filter(x->x.getUser().equals(user)).collect(Collectors.toCollection(ArrayList<BookFile>::new));
    }

    @RequestMapping(value = "/select/bookname/{bookname}", method = RequestMethod.GET)
    public ArrayList<BookFile> findByBookname(@PathVariable(required = true) String bookname) throws IOException {
        Collection<BookFile> allcontent = fileRepository.readFileSource().values();
//        allcontent.forEach(x->System.out.println(x.getBookname()));
        return allcontent.stream().filter(x->x.getBookname().contains(bookname)).collect(Collectors.toCollection(ArrayList<BookFile>::new));
    }





}

