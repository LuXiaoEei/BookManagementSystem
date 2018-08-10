package com.demo.service;

import com.demo.exception.BooknameNotFound;
import com.demo.exception.IsbnNotFound;
import com.demo.model.BookFile;
import com.demo.repository.FileRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;


@org.springframework.stereotype.Service()
@Qualifier("serviceFile")
public class ServiceFile implements Service {
//    private Path rootpath = Paths.get("", "src", "main", "java", "com", "demo", "file", "date");
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Autowired
    FileRepository fileRepository;

    @Override
    public BookFile addbook(String id, String bookname, String isbn, String category, String press, String user, String loantime, String returntime, String updatetime, HttpServletResponse response) throws IOException {
        BookFile bookFile = new BookFile(id, isbn, bookname, category, press, df.format(new Date()), "", "", "");
        return fileRepository.save(bookFile);
    }

    @Override
    public Object deleteBookByIsbn(String isbn, HttpServletResponse response) throws IOException {
        Collection<BookFile> allcontent = fileRepository.readFileSource().values();
        Collection<BookFile> target = allcontent.stream().filter(x -> !x.getIsbn().equals(isbn)).collect(Collectors.toCollection(ArrayList<BookFile>::new));
        fileRepository.delete();
        for (BookFile element : target) {
            fileRepository.save(element);
        }
        return allcontent.stream().filter(x -> x.getIsbn().equals(isbn)).collect(Collectors.toCollection(ArrayList<BookFile>::new));
    }

    @Override
    public Object updateBookByIsbn(String press, String category, String bookname, String isbn, String condition, HttpServletResponse response) throws IOException, IsbnNotFound, BooknameNotFound {
        Collection<BookFile> allcontent = fileRepository.readFileSource().values();
        ArrayList<BookFile> bookFile = new ArrayList<BookFile>();
        if (!StringUtils.isBlank(isbn) && isbn.equals("") | ((isbn.equals("''") | (isbn.equals("\"\""))))) {
            response.getWriter().println("don't set the isbn to null");
            throw new IsbnNotFound("isbn should be set validly");
        }
        if (!StringUtils.isBlank(bookname) && bookname.equals("") | ((bookname.equals("''") | (bookname.equals("\"\""))))) {
            response.getWriter().println("don't set the bookname to null");
            throw new BooknameNotFound("bookname should be set validly");
        }
        for (BookFile element : allcontent) {
            if (element.getIsbn().equals(condition)) {
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
                bookFile.add(element);
            }
        }
        fileRepository.delete();
        for (BookFile element : allcontent) {
            fileRepository.save(element);
        }
        return bookFile;
}
    @Override
    public Object describeBook( String start,  String nums, HttpServletResponse response) throws IOException {
        HashMap<Integer, BookFile> allcontent = fileRepository.readFileSource();
        ArrayList<BookFile> result = new ArrayList<BookFile>();
        Integer s=Integer.parseInt(start);
        Integer end = ((nums.equals("all")) ? allcontent.keySet().size() : Integer.valueOf(nums)) + s;
        for (Integer i = s; i < end; i++) {
            if (allcontent.get(i) != null) {
                result.add(allcontent.get(i));
            }
        }
        return result;
    }

    @Override
    public Object selectByBooknameLike(String bookname, HttpServletResponse response) throws IOException {
        Collection<BookFile> allcontent = fileRepository.readFileSource().values();
        return allcontent.stream().filter(x -> x.getBookname().contains(bookname)).collect(Collectors.toCollection(ArrayList<BookFile>::new));
    }

    @Override
    public Object selectByUser(String user, HttpServletResponse response) throws IOException {
        Collection<BookFile> allcontent = fileRepository.readFileSource().values();
        return allcontent.stream().filter(x -> x.getUser().equals(user)).collect(Collectors.toCollection(ArrayList<BookFile>::new));
    }

    @Override
    public void loanBookByUserAndIsbn(String user, String isbn, HttpServletResponse response) throws IOException, ParseException {
        Collection<BookFile> allcontent = fileRepository.readFileSource().values();
        Collection<BookFile> target1 = allcontent.stream().filter(x -> !(x.getIsbn().equals(isbn))).collect(Collectors.toCollection(ArrayList<BookFile>::new));
        Collection<BookFile> target = allcontent.stream().filter(x -> x.getIsbn().equals(isbn)).collect(Collectors.toCollection(ArrayList<BookFile>::new));
        boolean flag = false;
        for (BookFile element : target) {
            if (element.getLoantime().equals("") || (!element.getReturntime().equals("") && df.parse(element.getReturntime()).after(df.parse(element.getLoantime())))) {
                element.setLoantime(df.format(new Date()));
                element.setUser(user);
                flag = true;
                break;
            }
        }
        if (!flag) {
            response.getWriter().println("Sorry, the book has been loaned");
        } else {
            fileRepository.delete();
            for (BookFile element1 : target1) {
                fileRepository.save(element1);
            }
            for (BookFile element : target) {
                fileRepository.save(element);
            }
            response.getWriter().println("sucess borrow the book");
        }
    }

    @Override
    public Object returnbookByUserAndIsbn(String user, String isbn, HttpServletResponse response) throws IOException, ParseException {
        Collection<BookFile> allcontent = fileRepository.readFileSource().values();
        Collection<BookFile> target1 = allcontent.stream().filter(x -> !(x.getIsbn().equals(isbn) && x.getUser().equals(user))).collect(Collectors.toCollection(ArrayList<BookFile>::new));
        Collection<BookFile> target = allcontent.stream().filter(x -> x.getIsbn().equals(isbn) && x.getUser().equals(user)).collect(Collectors.toCollection(ArrayList<BookFile>::new));
        BookFile temp=new BookFile();
        if (!target.isEmpty()){
            for (BookFile temp1:target){
                if (!temp1.getLoantime().equals("") && temp1.getReturntime().equals("") || df.parse(temp1.getReturntime()).before(df.parse(temp1.getLoantime()))) {
                    temp = temp1;
                    temp.setUser(user);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    temp.setReturntime(df.format(new Date()));
                    fileRepository.delete();
                    for (BookFile element1 : target1) {
                        fileRepository.save(element1);
                    }
                    for (BookFile element : target) {
                        fileRepository.save(element);
                    }
                    return "success to return!";
                }
            }
        }
        return "No books need to be return!";
    }
}
