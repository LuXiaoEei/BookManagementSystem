package com.demo.service;

import com.demo.error.BooknameNotFoundException;
import com.demo.model.BookFile;
import com.demo.repository.BookRepositoryFile;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Autowired
    private BookRepositoryFile bookRepositoryFile;

    @Override
    public BookFile addbook(String id, String bookname, String isbn, String category, String press, String user, String loantime, String returntime, String updatetime, HttpServletResponse response) throws IOException{
        BookFile bookFile = new BookFile(id, isbn, bookname, category, press, df.format(new Date()), "", "", "");
        return bookRepositoryFile.save(bookFile);
    }

    @Override
    public Object deleteBookByIsbn(String isbn, HttpServletResponse response) throws IOException {
        Collection<BookFile> allcontent = bookRepositoryFile.readFileSource().values();
        Collection<BookFile> target = allcontent.stream().filter(x -> !x.getIsbn().equals(isbn)).collect(Collectors.toCollection(ArrayList<BookFile>::new));
        bookRepositoryFile.delete();
        for (BookFile element : target) {
            bookRepositoryFile.save(element);
        }
        return allcontent.stream().filter(x -> x.getIsbn().equals(isbn)).collect(Collectors.toCollection(ArrayList<BookFile>::new));
    }

    @Override
    public Object updateBookByIsbn(String press, String category, String bookname, String isbn, String condition, HttpServletResponse response) throws IOException, BooknameNotFoundException {
        Collection<BookFile> allcontent = bookRepositoryFile.readFileSource().values();
        ArrayList<BookFile> bookFile = new ArrayList<BookFile>();
        if (!StringUtils.isBlank(bookname) && bookname.replaceAll(" ","").equals("") | ((bookname.replaceAll(" ","").equals("''") | (bookname.replaceAll(" ","").equals("\"\""))))) {
            throw new BooknameNotFoundException("don't set the bookname to null");
        }
        for (BookFile element : allcontent) {
            if (element.getIsbn().equals(condition)) {
                if (!StringUtils.isBlank(bookname))
                    element.setBookname(bookname);
                if (!StringUtils.isBlank(press))
                    element.setPress((press.equals("''") | (press.equals("\"\""))) ? "" : press);
                if (!StringUtils.isBlank(category))
                    element.setCategory((category.equals("''") | (category.equals("\"\""))) ? "" : category);
                element.setIsbn(isbn);
                element.setUpdatetime(df.format(new Date()));
                bookFile.add(element);
            }
        }
        bookRepositoryFile.delete();
        for (BookFile element : allcontent) {
            bookRepositoryFile.save(element);
        }
        return bookFile;
}

    @Override
    public Integer countAll() throws IOException {
        try{
            return bookRepositoryFile.readFileSource().size();
        }catch (FileNotFoundException e){
            return 0;
        }
    }

    @Override
    public Object describeBook( String start,  String nums, HttpServletResponse response) throws IOException {
        HashMap<Integer, BookFile> allcontent = bookRepositoryFile.readFileSource();
        ArrayList<BookFile> result = new ArrayList<BookFile>();
        Integer s = Integer.valueOf(start);
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
        Collection<BookFile> allcontent = bookRepositoryFile.readFileSource().values();
        return allcontent.stream().filter(x -> x.getBookname().contains(bookname)).collect(Collectors.toCollection(ArrayList<BookFile>::new));
    }

    @Override
    public Object selectByUser(String user, HttpServletResponse response) throws IOException, ParseException {
        Collection<BookFile> allcontent = bookRepositoryFile.readFileSource().values().stream().filter(x -> x.getUser().equals(user)).collect(Collectors.toCollection(ArrayList<BookFile>::new));;
        Collection<BookFile> result1=new ArrayList<>();
        if (!allcontent.isEmpty()) {
            for (BookFile temp : allcontent) {
                if (!flag(temp)) {
                    result1.add(temp);
                }
            }
        }
        return result1;
    }

    @Override
    public void loanBookByUserAndIsbn(String user, String isbn, HttpServletResponse response) throws IOException, ParseException {
        Collection<BookFile> allcontent = bookRepositoryFile.readFileSource().values();
        Collection<BookFile> target1 = allcontent.stream().filter(x -> !(x.getIsbn().equals(isbn))).collect(Collectors.toCollection(ArrayList<BookFile>::new));
        Collection<BookFile> target = allcontent.stream().filter(x -> x.getIsbn().equals(isbn)).collect(Collectors.toCollection(ArrayList<BookFile>::new));
        boolean flag = false;
        for (BookFile element : target) {
            if (flag(element)) {
                element.setLoantime(df.format(new Date()));
                element.setUser(user);
                flag = true;
                break;
            }
        }
        if (!flag) {
            response.getWriter().println("Sorry, the book has been loaned");
            response.flushBuffer();
        } else {
            bookRepositoryFile.delete();
            for (BookFile element1 : target1) {
                bookRepositoryFile.save(element1);
            }
            for (BookFile element : target) {
                bookRepositoryFile.save(element);
            }
            response.getWriter().println("sucess borrow the book");
            response.flushBuffer();
        }
    }

    @Override
    public Object returnbookByUserAndIsbn(String user, String isbn, HttpServletResponse response) throws IOException, ParseException {
        Collection<BookFile> allcontent = bookRepositoryFile.readFileSource().values();
        Collection<BookFile> target1 = allcontent.stream().filter(x -> !(x.getIsbn().equals(isbn) && x.getUser().equals(user))).collect(Collectors.toCollection(ArrayList<BookFile>::new));
        Collection<BookFile> target = allcontent.stream().filter(x -> x.getIsbn().equals(isbn) && x.getUser().equals(user)).collect(Collectors.toCollection(ArrayList<BookFile>::new));
        if (!target.isEmpty()){
            for (BookFile temp:target){
                if (!flag(temp)) {
                    temp.setUser(user);
                    temp.setReturntime(df.format(new Date()));
                    bookRepositoryFile.delete();
                    for (BookFile element1 : target1) {
                        bookRepositoryFile.save(element1);
                    }
                    for (BookFile element : target) {
                        bookRepositoryFile.save(element);
                    }
                    return "Success to return!";
                }
            }
        }
        return "No books need to be returned!";
    }
/*flag indicates the state of book:true means availability*/
    public boolean flag (BookFile element) throws ParseException {
        return "".equals(element.getLoantime())||(!"".equals(element.getReturntime())&& df.parse(element.getReturntime()).after(df.parse(element.getLoantime())));
    }
}
