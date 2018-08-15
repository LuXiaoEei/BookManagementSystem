package com.demo.service;

import com.demo.error.BooknameNotFoundException;
import com.demo.error.IsbnNotFoundException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

/**
 * 接口里面的方法默认是公共的，public关键字不需要加
 * service命名太草率了，最好一眼就能看出来做的什么业务，比如：BookService
 */
public interface Service {
    public Object addbook(String id , String bookname, String isbn, String  category, String press, String user, String loantime, String returntime, String updatetime, HttpServletResponse response) throws IOException, IsbnNotFoundException;

    public Object deleteBookByIsbn(String isbn,HttpServletResponse response) throws IOException;

    public Object updateBookByIsbn(String press, String category, String bookname, String isbn, String condition, HttpServletResponse response) throws IsbnNotFoundException, BooknameNotFoundException, IOException;

    public Integer countAll() throws IOException;

    public Object describeBook(String start, String nums,HttpServletResponse response) throws IOException;

    public Object selectByBooknameLike(String bookname,HttpServletResponse response) throws IOException;

    public Object selectByUser(String user,HttpServletResponse response) throws IOException, ParseException;

    public void loanBookByUserAndIsbn(String user,String isbn,HttpServletResponse response) throws IOException, ParseException;

    public Object returnbookByUserAndIsbn(String user,String isbn,HttpServletResponse response) throws IOException, ParseException;

}
