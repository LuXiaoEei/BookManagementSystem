package com.demo.file.server;

import com.demo.server.Server;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class ServerFile implements Server {

    @Override
    public String addbook(String id, String bookname, String isbn, String category, String press, String user, String loantime, String returntime, String updatetime, HttpServletResponse response) throws IOException {
        return null;
    }

    @Override
    public Object deleteBookByIsbn(String isbn, HttpServletResponse response) {
        return null;
    }

    @Override
    public Object updateBookByIsbn(String press, String category, String bookname, String isbn, String condition, HttpServletResponse response) {
        return null;
    }

    @Override
    public Object describeBook(String start, String nums, HttpServletResponse response) {
        return null;
    }

    @Override
    public Object selectByBooknameLike(String bookname, HttpServletResponse response) {
        return null;
    }

    @Override
    public Object selectByUser(String user, HttpServletResponse response) {
        return null;
    }

    @Override
    public void loanBookByUserAndIsbn(String user, String isbn, HttpServletResponse response) throws IOException {

    }

    @Override
    public Object returnbookByUserAndIsbn(String user, String isbn, HttpServletResponse response) throws IOException {
        return null;
    }
}
