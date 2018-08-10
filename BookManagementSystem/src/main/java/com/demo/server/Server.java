package com.demo.server;

import com.demo.error.BooknameNotFound;
import com.demo.error.IsbnNotFound;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Predicate;

public interface Server {
    public String addbook(String id , String bookname, String isbn, String  category, String press, String user, String loantime, String returntime, String updatetime,HttpServletResponse response) throws IOException;

    public Object deleteBookByIsbn(String isbn,HttpServletResponse response);

    public Object updateBookByIsbn(String press, String category, String bookname, String isbn, String condition, HttpServletResponse response) throws IsbnNotFound, BooknameNotFound;

    public Object describeBook(String start, String nums,HttpServletResponse response);

    public Object selectByBooknameLike(String bookname,HttpServletResponse response);

    public Object selectByUser(String user,HttpServletResponse response);

    public void loanBookByUserAndIsbn(String user,String isbn,HttpServletResponse response) throws IOException;

    public Object returnbookByUserAndIsbn(String user,String isbn,HttpServletResponse response) throws IOException;

}
