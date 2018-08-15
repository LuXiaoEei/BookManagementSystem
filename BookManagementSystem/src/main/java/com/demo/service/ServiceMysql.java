package com.demo.service;

import com.demo.error.BooknameNotFoundException;
import com.demo.error.IsbnNotFoundException;
import com.demo.model.BookMysql;
import com.demo.repository.BookRepositoryMysql;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * 实现类的命名一般叫**ServiceImpl
 * 有一些同样的毛病参考ServiceGemfire我加的注释
 */
@org.springframework.stereotype.Service()
@Qualifier("serviceMysql")
public class ServiceMysql implements Service {

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

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
        response.flushBuffer();
        return null;
    }

    @Override
    public Object deleteBookByIsbn(String isbn, HttpServletResponse response) {
        return bookRepositoryMysql.deleteByIsbnEquals(isbn);
    }

    @Override
    public Object updateBookByIsbn(String press, String category, String bookname, String isbn, String condition, HttpServletResponse response) throws IsbnNotFoundException, BooknameNotFoundException {
        if (!StringUtils.isBlank(isbn) &&isbn.equals ("")|((isbn.equals("''") | (isbn.equals("\"\""))))) {
            throw new IsbnNotFoundException("don't set the isbn to null");
        }
        if (!StringUtils.isBlank(bookname) && bookname.replaceAll(" ","").equals("") | ((bookname.replaceAll(" ","").equals("''") | (bookname.replaceAll(" ","").equals("\"\""))))) {
            throw new BooknameNotFoundException("don't set the bookname to null");
        }
        String datetime = df.format(new Date());
        if (!StringUtils.isBlank(bookname)){
            /**
             * todo 这里的三目运算看起来毫无意义？？？  几个更新是更新同一张表 没必要写多个sql、更新多次 尽量减少与数据库交互次数，提高效率
             * 这里改成一条sql 只更新一次
             */
            bookRepositoryMysql.updateBookname((bookname.equals("''") | (bookname.equals("\"\""))) ? "" : bookname, datetime, condition);
        }
        if (!StringUtils.isBlank(category)){
            bookRepositoryMysql.updateCategory((category.equals("''") | (category.equals("\"\""))) ? "" : category, datetime, condition);
        }
        if (!StringUtils.isBlank(press)){
            bookRepositoryMysql.updatePress((press.equals("''") | (press.equals("\"\""))) ? "" : press, datetime, condition);
        }
        if (!StringUtils.isBlank(isbn)) {
            bookRepositoryMysql.updateIsbn(isbn, datetime, condition);
        }
        String tmpisbn = (StringUtils.isBlank(isbn)) ? condition : isbn;
        return bookRepositoryMysql.selectByIsbn(tmpisbn);
    }

    @Override
    public Integer countAll(){
        return bookRepositoryMysql.countAll();
    }

    @Override
    public Object describeBook(String start, String nums, HttpServletResponse response) {
        int starts = Integer.valueOf(start);
        int num = (nums.equals("all"))?bookRepositoryMysql.countAll():Integer.valueOf(nums);
        return bookRepositoryMysql.describe(starts,num);
    }

    @Override
    public Object selectByBooknameLike(String bookname, HttpServletResponse response) {
        return bookRepositoryMysql.findByBooknameContains(bookname);
    }

    @Override
    public Object selectByUser(String user, HttpServletResponse response) {
        return bookRepositoryMysql.findByUserEquals(user);
    }

    @Override
    public void loanBookByUserAndIsbn(String user, String isbn, HttpServletResponse response) throws IOException {
        Collection<BookMysql> res = bookRepositoryMysql.serachNoloanedBookByIsbn(isbn);
        if (res.isEmpty()){
            response.getWriter().println("no book remained");
            response.flushBuffer();
        }else{
            Long id =res.iterator().next().getId();
            bookRepositoryMysql.loanBook(user,df.format(new Date()),id);
            response.getWriter().println(user+" success borrowing isbn: "+ isbn +" at "+df.format(new Date()));
            response.flushBuffer();
        }
    }

    @Override
    /**
     * 返回内容参考我在controller里面加的注释
     */
    public Object returnbookByUserAndIsbn(String user, String isbn, HttpServletResponse response) throws IOException {
        Collection<BookMysql> res = bookRepositoryMysql.serachloanedBookByBooknameAndIsbn(user,isbn);
        if (res.isEmpty()){
            response.getWriter().println("you have not borrowed the book");
            response.flushBuffer();
        }else{
            bookRepositoryMysql.returnBook(res.iterator().next().getId());
            response.getWriter().println(user+" success returning isbn: "+ isbn +" at "+ df.format(new Date()));
            response.flushBuffer();
        }
        return null;
    }
}
