package com.demo.past.geode.controller;

import com.demo.model.BookGemfire;
import com.demo.repository.BookRepositoryGemfire;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/1" ,method = RequestMethod.GET)
public class GemfireController {

    @Autowired
    private BookRepositoryGemfire bookRepositoryGemfire;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @ExceptionHandler(Exception.class)
    public void processMethod(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("exception: " + ex.getMessage());
        response.getWriter().printf("exception: " + ex.getMessage());
        response.flushBuffer();
    }

    @RequestMapping(value = "/add", method = {RequestMethod.GET,RequestMethod.POST})
    public String addBook(@RequestParam(value = "id", required = true) String id,
                          @RequestParam(value = "isbn", required = true) String isbn,
                          @RequestParam(value = "press", required = false, defaultValue = "") String press,
                          @RequestParam(value = "user", required = false, defaultValue = "") String user,
                          @RequestParam(value = "loantime", required = false, defaultValue = "") String loantime,
                          @RequestParam(value = "bookname", required = true) String bookname,
                          @RequestParam(value = "category", required = false, defaultValue = "") String category,
                          @RequestParam(value = "returntime", required = false, defaultValue = "") String returntime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        BookGemfire p = new BookGemfire(id, bookname, isbn, category, press, user, loantime, returntime, df.format(new Date()));
        bookRepositoryGemfire.save(p);
        return p.toString();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Collection<BookGemfire> deleteBook(@RequestParam(value = "isbn", required = true) String isbn) {
        Collection<BookGemfire> result = bookRepositoryGemfire.findByIsbn(isbn);
        for (BookGemfire element : result) {
            bookRepositoryGemfire.deleteById(element.getId());
        }
        return result;
    }

    @RequestMapping(value = "/select/bookname/{bookname}", method = RequestMethod.GET)
    public Collection<BookGemfire> findBook(@PathVariable(required = true) String bookname) {
        Collection<BookGemfire> result = bookRepositoryGemfire.findByBooknameLike("%" + bookname + "%");
        return result;
    }

    @RequestMapping(value = "/select/user/{user}", method = RequestMethod.GET)
    public Collection<BookGemfire> findUser(@PathVariable(required = true) String user) {
        Collection<BookGemfire> result = bookRepositoryGemfire.findByUser(user);
        return result;
    }

    @RequestMapping(value = "/describe", method = RequestMethod.GET)
    public Collection<BookGemfire> describe(@RequestParam(value = "nums", required = false, defaultValue = "-1") int num) {
        Collection<BookGemfire> result = bookRepositoryGemfire.describe(num);
        return result;
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public Collection<BookGemfire> updateBook(@RequestParam(value = "press", required = false) String press,
                                              @RequestParam(value = "category", required = false, defaultValue = "") String category,
                                              @RequestParam(value = "bookname", required = false) String bookname,
                                              @RequestParam(value = "isbn", required = false) String isbn,
                                              @RequestParam(value = "condition", required = true) String condition) {
        Collection<BookGemfire> result = bookRepositoryGemfire.findByIsbn(condition);
        if (!StringUtils.isBlank(bookname))
            for (BookGemfire element : result) {
                element.setBookname(bookname);
            }
        if (!StringUtils.isBlank(press))
            for (BookGemfire element : result) {
                element.setPress(press);
            }
        if (!StringUtils.isBlank(category))
            for (BookGemfire element : result) {
                element.setCategory(category);
            }
        if (!StringUtils.isBlank(isbn))
            for (BookGemfire element : result) {
                element.setIsbn(isbn);
            }
        bookRepositoryGemfire.saveAll(result);
        return result;
    }

    @RequestMapping(value = "/loanbook", method = RequestMethod.GET)
    public String loanBook(@RequestParam(value = "isbn", required = true) String isbn,
                           @RequestParam(value = "user", required = true) String user) throws ParseException {
        Collection<BookGemfire> result = bookRepositoryGemfire.findByIsbn(isbn);
        for (BookGemfire element : result) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            if (element.getLoantime().equals("") || !element.getReturntime().equals("") && df.parse(element.getReturntime()).after(df.parse(element.getLoantime()))) {
                element.setLoantime(df.format(new Date()));
                element.setUser(user);
                bookRepositoryGemfire.save(element);
                return element.toString();
            }
        }
        return "Sorry, the book has been loaned";
    }

    @RequestMapping(value = "/returnbook", method = RequestMethod.GET)
    public String returnBook(@RequestParam(value = "isbn", required = true) String isbn,
                             @RequestParam(value = "user", required = true) String user) throws ParseException {
        Collection<BookGemfire> result = bookRepositoryGemfire.describe(-1);
        result = result.stream().filter(book -> book.getIsbn().equals(isbn) & book.getUser().equals(user)).collect(Collectors.toList());
        if (!result.isEmpty()) {
            for (BookGemfire temp : result) {
                if (!temp.getLoantime().equals("") && temp.getReturntime().equals("") || df.parse(temp.getReturntime()).before(df.parse(temp.getLoantime()))) {
                    temp.setUser(user);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    temp.setReturntime(df.format(new Date()));
                    bookRepositoryGemfire.save(temp);
                    return result.toString();
                }
            }
        }
        return "No books need to be return!";
    }
}
