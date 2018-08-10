package com.demo.geode.server;

import com.demo.error.BooknameNotFound;
import com.demo.error.DatabaseError;
import com.demo.error.IdError;
import com.demo.error.IsbnNotFound;
import com.demo.geode.model.BookGemfire;
import com.demo.geode.repository.BookRepositoryGemfire;
import com.demo.mysql.model.BookMysql;
import com.demo.server.Server;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service("Gemfire")
public class ServerGemfire implements Server {
    @Autowired
    private BookRepositoryGemfire bookRepositoryGemfire;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private String isVaildId(String id) throws IdError {
        String regex = "[^0-9]";
        if (Pattern.compile(regex).matcher(id).find()) {
            throw new IdError(id + "is not a vaild id");
        } else {
            return id;
        }
    }

    private String isVaildIsbn(String isbn) throws IsbnNotFound {
        String regex = "[^A-Za-z0-9]";
        if (!Pattern.compile(regex).matcher(isbn).find()) {
            if (isbn.length() == 12) {
                if (isbn.startsWith("978")) {
                    return isbn;
                } else {
                    throw new IsbnNotFound(isbn + "is not vaild");
                }
            }
            if (isbn.length() == 9) {
                return "978" + isbn;
            }
        }
        throw new IsbnNotFound(isbn + "is not vaild");
    }

    @Override
    public String addbook(String id, String bookname, String isbn, String category, String press, String user, String loantime, String returntime, String updatetime, HttpServletResponse response) throws  IdError, IsbnNotFound {
        if (id.equals("")) {
            //response.getWriter().println("Error: in Gemfire model, you must input id!");
            throw new IdError("Error: in Gemfire model, you must input id!");
        }
        if (isbn.equals("") | ((isbn.equals("''") | (isbn.equals("\"\""))))) {
            //response.getWriter().println("Error: in Gemfire model, you must input id!");
            throw new IsbnNotFound("Error: isbn is needed");
        }
        BookGemfire bookGemfire = new BookGemfire(isVaildId(id), bookname, isVaildIsbn(isbn), category, press, user, loantime, returntime, df.format(new Date()));
        bookRepositoryGemfire.save(bookGemfire);
        return bookGemfire.toString();
    }

    @Override
    public Object deleteBookByIsbn(String isbn, HttpServletResponse response) {
        Collection<BookGemfire> result = bookRepositoryGemfire.findByIsbn(isbn);

        for (BookGemfire element : result) {
            bookRepositoryGemfire.deleteById(element.getId());
        }
        return result;
    }

    @Override
    public Object updateBookByIsbn(String press, String category, String bookname, String isbn, String condition, HttpServletResponse response) throws IOException, IsbnNotFound, BooknameNotFound {
        if (!StringUtils.isBlank(isbn) && isbn.equals("") | ((isbn.equals("''") | (isbn.equals("\"\""))))) {
            response.getWriter().println("don't set the isbn to null");
            throw new IsbnNotFound("isbn should be set validly");
        }
        if (!StringUtils.isBlank(bookname) && bookname.equals("") | ((bookname.equals("''") | (bookname.equals("\"\""))))) {
            response.getWriter().println("don't set the bookname to null");
            throw new BooknameNotFound("bookname should be set validly");
        } else {
            String datetime = df.format(new Date());
            Collection<BookGemfire> result = bookRepositoryGemfire.findByIsbn(condition);
            if (!StringUtils.isBlank(bookname))
                for (BookGemfire element : result) {
                    element.setBookname((bookname.equals("''") | (bookname.equals("\"\""))) ? "" : bookname);
                    element.setUpdatetime(datetime);
                }
            if (!StringUtils.isBlank(press))
                for (BookGemfire element : result) {
                    element.setPress((press.equals("''") | (press.equals("\"\""))) ? "" : press);
                    element.setUpdatetime(datetime);
                }
            if (!StringUtils.isBlank(category))
                for (BookGemfire element : result) {
                    element.setCategory((category.equals("''") | (category.equals("\"\""))) ? "" : category);
                    element.setUpdatetime(datetime);
                }
            if (!StringUtils.isBlank(isbn))
                for (BookGemfire element : result) {
                    element.setIsbn(isbn);
                    element.setUpdatetime(datetime);
                }
            bookRepositoryGemfire.saveAll(result);
            return result;
        }
    }

    @Override
    public Object describeBook(String start, String nums, HttpServletResponse response) {
        int num1 = (nums.equals("all")) ? -1 : Integer.valueOf(nums);
        return bookRepositoryGemfire.describe(num1);
    }

    @Override
    public Object selectByBooknameLike(String bookname, HttpServletResponse response) {
        return bookRepositoryGemfire.findByBooknameLike("%" + bookname + "%");
    }

    @Override
    public Object selectByUser(String user, HttpServletResponse response) {
        return bookRepositoryGemfire.findByUser(user);
    }

    @Override
    public void loanBookByUserAndIsbn(String user, String isbn, HttpServletResponse response) throws IOException, ParseException {
        boolean tag=false;
        Collection<BookGemfire> result = bookRepositoryGemfire.findByIsbn(isbn);
        for (BookGemfire element : result) {
            if (element.getLoantime().equals("")||!element.getReturntime().equals("") && df.parse(element.getReturntime()).after(df.parse(element.getLoantime()))) {
                element.setLoantime(df.format(new Date()));
                element.setUser(user);
                bookRepositoryGemfire.save(element);
                response.getWriter().println(user+" success borrowing isbn: s"+ isbn +" at "+df.format(new Date()));
                tag=true;
                break;
            }
        }
        if (!tag) response.getWriter().println("no book remained");

    }

    @Override
    public Object returnbookByUserAndIsbn(String user, String isbn, HttpServletResponse response) throws IOException {
        Collection<BookGemfire> result = bookRepositoryGemfire.describe(-1);
        result = result.stream().filter(book -> book.getIsbn().equals(isbn) & book.getUser().equals(user)).collect(Collectors.toList());
        boolean flag = false;
        if (!result.isEmpty()) {
            BookGemfire temp1 = result.iterator().next();
            if (!temp1.getLoantime().equals("") && temp1.getReturntime().equals("") || temp1.getReturntime().equals("")) {
                BookGemfire temp = result.iterator().next();
                temp.setUser(user);
                temp.setReturntime(df.format(new Date()));
                bookRepositoryGemfire.save(temp);
                response.getWriter().println(user + " success returning isbn: " + isbn + " at " + df.format(new Date()));
                flag = true;
                return null;
            }
        }
        if (!flag) {
            response.getWriter().println("you have not borrowed the book");
            return null;
        }
        return null;
    }
}

