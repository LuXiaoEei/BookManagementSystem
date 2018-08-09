package com.demo.file.controller;


import com.demo.file.model.BookFile;
import com.demo.file.repository.FileRepository;
import com.demo.geode.model.BookGemfire;
import com.demo.geode.repository.BookRepositoryGemfire;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import  java.io.*;
import java.lang.System;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
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

    @ExceptionHandler(MissingServletRequestParameterException.class)
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
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ArrayList<BookFile> deleteBook(@RequestParam(value = "isbn", required = true) String isbn,
                             HttpServletResponse response) throws IOException {
        Collection<BookFile> allcontent = fileRepository.readFileSource().values();
        Collection<BookFile> target=allcontent.stream().filter(x->!x.getIsbn().equals(isbn)).collect(Collectors.toCollection(ArrayList<BookFile>::new));
        fileRepository.delete();
        for (BookFile element:target) {
            fileRepository.save(element);
        }
        return allcontent.stream().filter(x->x.getUser().equals(isbn)).collect(Collectors.toCollection(ArrayList<BookFile>::new));
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public BookFile updateBook(@RequestParam(value = "press", required = false) String press,
                             @RequestParam(value = "category", required = false,defaultValue = "") String category,
                             @RequestParam(value = "bookname", required = false) String bookname,
                             @RequestParam(value = "isbn", required = false) String isbn,
                             @RequestParam(value = "condition", required = true) String condition,
                             HttpServletResponse response) throws IOException {
        Collection<BookFile> allcontent = fileRepository.readFileSource().values();
        BookFile bookFile = new BookFile();
        for (BookFile element:allcontent) {
            if(element.getIsbn().equals(isbn)){
                if (!StringUtils.isBlank(bookname))
                    element.setBookname(bookname);
                if (!StringUtils.isBlank(press))
                    element.setPress((press.equals("''") | (press.equals("\"\""))) ? "" : press);
                if (!StringUtils.isBlank(category))
                        element.setCategory((category.equals("''") | (category.equals("\"\""))) ? "" : category);
                if (!StringUtils.isBlank(isbn))
                        element.setIsbn(isbn);
                String datetime = df.format(new Date());
                element.setUpdatetime(datetime);
                bookFile=element;
            }
        }
        fileRepository.delete();
        for (BookFile element:allcontent) {
            fileRepository.save(element);
        }
        return bookFile;
    }
    @RequestMapping(value = "/loanbook", method = RequestMethod.GET)
    public String loanBook(@RequestParam(value = "isbn", required = true) String isbn,
                         @RequestParam(value = "user", required = true) String user,
                         HttpServletResponse response) throws IOException, ParseException {
        Collection<BookFile> allcontent = fileRepository.readFileSource().values();
        Collection<BookFile> target1=allcontent.stream().filter(x->!(x.getIsbn().equals(isbn)&&x.getUser().equals(user))).collect(Collectors.toCollection(ArrayList<BookFile>::new));
        Collection<BookFile> target=allcontent.stream().filter(x->x.getIsbn().equals(isbn)&&x.getUser().equals(user)).collect(Collectors.toCollection(ArrayList<BookFile>::new));
        for (BookFile element : target) {
            if (element.getLoantime().equals("")||!element.getReturntime().equals("") && df.parse(element.getReturntime()).after(df.parse(element.getLoantime()))) {
                element.setLoantime(df.format(new Date()));
                element.setUser(user);
                break;
            }
        }
        for (BookFile element1:target1) {
            fileRepository.save(element1);
        }
        for (BookFile element:target) {
            fileRepository.save(element);
        }
        return "Sorry, the book has been loaned";
    }
    @RequestMapping(value = "/returnbook", method = RequestMethod.GET)
    public String returnBook(@RequestParam(value = "isbn", required = true) String isbn,
                                              @RequestParam(value = "user", required = true) String user) throws ParseException, IOException {
        Collection<BookFile> allcontent = fileRepository.readFileSource().values();
        Collection<BookFile> target1=allcontent.stream().filter(x->!(x.getIsbn().equals(isbn)&&x.getUser().equals(user))).collect(Collectors.toCollection(ArrayList<BookFile>::new));
        Collection<BookFile> target=allcontent.stream().filter(x->x.getIsbn().equals(isbn)&&x.getUser().equals(user)).collect(Collectors.toCollection(ArrayList<BookFile>::new));
        BookFile temp=target.iterator().next();
        temp.setUser(user);
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        temp.setReturntime(df.format(new Date()));
        for (BookFile element1:target1) {
            fileRepository.save(element1);
        }
        fileRepository.save(temp);
        return temp.toString();
    }




}

