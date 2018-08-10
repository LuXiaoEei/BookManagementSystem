package com.demo.mysql.server;

import com.demo.error.IdError;
import com.demo.mysql.model.BookMysql;
import com.demo.mysql.repository.BookRepositoryMysql;
import com.demo.server.Server;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class ServerMysql implements Server {

    @Autowired
    private BookRepositoryMysql bookRepositoryMysql;

    @Override
    public String addbook(String id, String bookname, String isbn, String category, String press, String user, String loantime, String returntime, String updatetime, HttpServletResponse response) throws IOException {
        BookMysql bookMysql = new BookMysql(isbn, bookname, press, category, new Date(), user, loantime,returntime);
        bookRepositoryMysql.save(bookMysql);
        if (!id.equals("")) {
            response.getWriter().println("Warning: in Mysql model, your input id will not work, it will be automatic assigned !");
            response.getWriter().println("<br/>");
        }
            response.getWriter().println(bookMysql.toString());
        return null;
    }

    @Override
    public Object deleteBookByIsbn(String isbn, HttpServletResponse response) {
        return null;
    }

    @Override
    public Object updateBookByIsbn(String isbn, HttpServletResponse response) {
        return null;
    }

    @Override
    public Object describeBook(int starts, int num, HttpServletResponse response) {
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
    public void loanBookByUserAndIsbn(String user, String isbn, HttpServletResponse response) {

    }

    @Override
    public Object returnbookByUserAndIsbn(String user, String isbn, HttpServletResponse response) {
        return null;
    }
}
