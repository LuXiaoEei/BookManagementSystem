package com.demo.model;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name="Book")
public class BookMysql implements Serializable {
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column
    private String isbn;
    @Column
    private String bookname;
    @Column
    private String press;
    @Column
    private String  category;
    @Column
    private Date updatetime;
    @Column
    private String user;
    @Column
    private String loantime;
    @Column
    private String returntime;


    public BookMysql(){};

    public BookMysql(String isbn, String bookname, String press, String category, Date updatetime,String user,String loantime,String returntime) {
        this.isbn = isbn;
        this.bookname = bookname;
        this.press=press;
        this.category = category;
        this.updatetime = updatetime;
        this.user=user;
        this.loantime=loantime;
        this.returntime=returntime;
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLoantime() {
        return loantime;
    }

    public void setLoantime(String lendtime) {
        this.loantime = loantime;
    }

    public String getReturntime() {
        return returntime;
    }

    public void setReturntime(String returntime) {
        this.returntime = returntime;
    }

    public String getUpdatetime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return df.format(updatetime);
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "BookMysqlString{" +
                "id=" + id +
                ", isbn=" + isbn +'\'' +
                ", bookname='" + bookname + '\'' +
                ", press='" + press + '\'' +
                ", category=" + category +
                ", updatetime='" + getUpdatetime()+ '\'' +
                ", user='" + user+ '\'' +
                ", loantime='" + loantime+ '\'' +
                ", returntime='" + returntime+ '\'' +
                '}';
    }
}