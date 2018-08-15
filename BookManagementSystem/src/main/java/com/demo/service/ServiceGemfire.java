package com.demo.service;

import com.demo.error.BooknameNotFoundException;
import com.demo.model.BookGemfire;
import com.demo.repository.BookRepositoryGemfire;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service()
@Qualifier("serviceGemfire")
public class ServiceGemfire implements Service {

    @Autowired
    private BookRepositoryGemfire bookRepositoryGemfire;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    public String addbook(String id, String bookname, String isbn, String category, String press, String user, String loantime, String returntime, String updatetime, HttpServletResponse response) {
        BookGemfire bookGemfire = new BookGemfire(id, bookname, isbn, category, press, user, loantime, returntime, df.format(new Date()));
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
    /**
     * 这个方法可读性略差   同一个集合遍历了多次  不知所云
     * 看了下这个判断形式  为什么不能外层遍历 内层用if判断再去set
     * 既然无论如何要element.setUpdatetime(datetime); 前面的set可以不要
     */
    public Object updateBookByIsbn(String press, String category, String bookname, String isbn, String condition, HttpServletResponse response) throws IOException,BooknameNotFoundException {
        if (!StringUtils.isBlank(bookname) && bookname.replaceAll(" ","").equals("") | ((bookname.replaceAll(" ","").equals("''") | (bookname.replaceAll(" ","").equals("\"\""))))) {
            throw new BooknameNotFoundException("don't set the bookname to null");
        }
        String datetime = df.format(new Date());
        Collection<BookGemfire> result = bookRepositoryGemfire.findByIsbn(condition);
        if (!StringUtils.isBlank(bookname))
            for (BookGemfire element : result) {
                /**
                 * 用||判断"或"，前面条件满足后不会再判断后面的条件，&&同理
                 */
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
        for (BookGemfire element : result) {
            element.setIsbn(isbn);
            element.setUpdatetime(datetime);
        }
        bookRepositoryGemfire.saveAll(result);
        return result;
    }

    @Override
    public Integer countAll(){
       return bookRepositoryGemfire.countAll();
    }

    @Override
    public Object describeBook(String start, String nums, HttpServletResponse response) {
        /**
         * .equals方法把常量放前面减少空指针异常，养成这个习惯
         */
        int num1 = (nums.equals("all")) ? -1 : Integer.valueOf(nums);
        return bookRepositoryGemfire.describe(num1);
    }

    @Override
    public Object selectByBooknameLike(String bookname, HttpServletResponse response) {
        /**
         * 在查询前判断条件可用性，既然新增时不允许书名为空查询时当然也不可以，以此来减少与数据库交互次数，减轻数据库负担
         */
        return bookRepositoryGemfire.findByBooknameLike("%" + bookname + "%");
    }

    @Override
    public Object selectByUser(String user, HttpServletResponse response) throws ParseException {
        /**
         * todo  这里把结果放进另一个集合的意义何在，如果为了过滤为什么不加在查询条件中
         * 另外  为什么要把这些记录过滤掉
         */
        Collection<BookGemfire> result =bookRepositoryGemfire.findByUser(user);
        Collection<BookGemfire> result1=new ArrayList<>();
        if (!result.isEmpty()) {
            for (BookGemfire temp : result) {
                if (!flag(temp)){
                    result1.add(temp);
                }
            }
        }
        return result1;
    }
    @Override
    public void loanBookByUserAndIsbn(String user, String isbn, HttpServletResponse response) throws IOException, ParseException {
        boolean tag=false;
        Collection<BookGemfire> result = bookRepositoryGemfire.findByIsbn(isbn);
        for (BookGemfire element : result) {
            if (flag(element)) {
                element.setLoantime(df.format(new Date()));
                element.setUser(user);
                bookRepositoryGemfire.save(element);
                response.getWriter().println(user+" success borrowing isbn: "+ isbn +" at "+df.format(new Date()));
                response.flushBuffer();
                tag=true;
                break;
            }
        }
        if (!tag) {
            response.getWriter().println("no book remained");
            response.flushBuffer();
        }

    }

    @Override
    /**
     * todo 驼峰命名法 returnbookByUserAndIsbn-->???
     */
    public Object returnbookByUserAndIsbn(String user, String isbn, HttpServletResponse response) throws ParseException {
        Collection<BookGemfire> result = bookRepositoryGemfire.describe(-1);
        result = result.stream().filter(book -> book.getIsbn().equals(isbn) & book.getUser().equals(user)).collect(Collectors.toList());
        if (!result.isEmpty()) {
            for (BookGemfire temp : result) {
                if (!flag(temp)) {
                    temp.setUser(user);
                    temp.setReturntime(df.format(new Date()));
                    bookRepositoryGemfire.save(temp);
                    return "Success to return!";
                }
            }
        }
        return "No books need to be return!";
    }
/* flag indicates the state of book:true means availability*/
    public boolean flag (BookGemfire element) throws ParseException {
        return "".equals(element.getLoantime())||(!"".equals(element.getReturntime())&& df.parse(element.getReturntime()).after(df.parse(element.getLoantime())));
    }
}

