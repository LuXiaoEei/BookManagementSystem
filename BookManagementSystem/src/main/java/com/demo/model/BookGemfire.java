package com.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.gemfire.mapping.annotation.Indexed;
import org.springframework.data.gemfire.mapping.annotation.LuceneIndexed;
import org.springframework.data.gemfire.mapping.annotation.Region;

@Region(name = "BookGemfire")
public class BookGemfire {
    @Id
    private String id;

    @Indexed
    private String bookname;
    private String isbn;
    private String category;
    private String user;
    private String press;

    @LuceneIndexed
    private String loantime;
    private String returntime;
    private String updatetime;

    @PersistenceConstructor
    public BookGemfire(String id, String bookname, String isbn, String category, String press, String user, String loantime, String returntime, String updatetime) {
        this.id = id;
        this.bookname = bookname;
        this.isbn = isbn;
        this.press=press;
        this.category = category;
        this.user=user;
        this.loantime = loantime;
        this.returntime = returntime;
        this.updatetime = updatetime;
    }
    public String getPress() { return press; }

    public void setPress(String press) { this.press = press; }

    public void setId(String id) { this.id = id; }

    public void setUser(String user) { this.user = user; }

    public void setLoantime(String loantime) { this.loantime = loantime; }

    public void setReturntime(String returntime) { this.returntime = returntime; }

    public String getId() { return id; }

    public String getUser() { return user; }

    public String getLoantime() { return loantime; }

    public String getReturntime() { return returntime; }

    public String getIsbn(){ return isbn; }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) { this.category = category; }


    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "BookGemfireString{" +
                "id=" + id +
                ",isbn='" + isbn + '\'' +
                ", bookname='" + bookname + '\'' +
                ", category=" + category +
                ", press=" + press +
                ", user=" + user +
                ", loantime=" + loantime +
                ", returntime=" + returntime +
                ", updatetime='" + updatetime+ '\'' +
                '}';
    }
}
