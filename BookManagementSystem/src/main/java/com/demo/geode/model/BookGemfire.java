package com.demo.geode.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.gemfire.mapping.annotation.Indexed;
import org.springframework.data.gemfire.mapping.annotation.LuceneIndexed;
import org.springframework.data.gemfire.mapping.annotation.Region;
import java.text.SimpleDateFormat;
import java.util.Date;

@Region(name = "BookGemfire")
//public class BookGemfire implements Serializable {
public class BookGemfire {

//    private static final long serialVersionUID = 1L;

    @Id
    private String isbn;

    @Indexed
    private String bookname;

    private String  category;

    @LuceneIndexed
    private String publicationyear;

    private String updatetime;

    @PersistenceConstructor
    public BookGemfire(String isbn, String bookname, String category, String publicationyear, String updatetime) {
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
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "BookGemfireString{" +
                "isbn=" + isbn +
                ", bookname='" + bookname + '\'' +
                ", category=" + category +
                ", publicationyear='" + publicationyear+ '\'' +
                ", updatetime='" + updatetime+ '\'' +
                '}';
    }
}
