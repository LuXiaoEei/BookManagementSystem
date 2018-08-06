package com.demo.mysql.model;

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
    private String  category;
    @Column
    private String publicationyear;
    @Column
    private Date updatetime;

    public BookMysql(){};

    public BookMysql(String isbn, String bookname, String category, String publicationyear, Date updatetime) {
        this.isbn = isbn;
        this.bookname = bookname;
        this.category = category;
        this.publicationyear = publicationyear;
        this.updatetime = updatetime;
    }

    public String getIsbn(){
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPublicationyear() {
        return publicationyear;
    }

    public void setPublicationyear(String publicationyear) {
        this.publicationyear = publicationyear;
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
                "id="+id+
                "isbn=" + isbn +'\'' +
                ", bookname='" + bookname + '\'' +
                ", category=" + category +
                ", publicationyear='" + publicationyear+ '\'' +
                ", updatetime='" + getUpdatetime()+ '\'' +
                '}';
    }
}