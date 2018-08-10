package com.demo.model;

public class BookFile {
    private String id;
    private String bookname;
    private String isbn;
    private String category;
    private String user;
    private String press;
    private String loantime;
    private String returntime;
    private String updatetime;

    public BookFile(String id, String isbn, String bookname, String category, String press, String updatetime,String user,String loantime,String returntime) {
        this.id = id;
        this.bookname = bookname;
        this.isbn = isbn;
        this.category = category;
        this.press=press;
        this.updatetime = updatetime;
        this.user=user;
        this.returntime=returntime;
        this.loantime=loantime;
    }
    public BookFile(String[] str){
        if (str.length==9){
            this.id = str[8];
            this.bookname = str[1];
            this.isbn = str[0];
            this.category = str[3];
            this.press=str[2];
            this.updatetime = str[4];
            this.user=str[5];
            this.returntime=str[7];
            this.loantime=str[6];
        }else{
            System.out.println("your input is invaild!");
        }
    }
    public BookFile (){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getLoantime() {
        return loantime;
    }

    public void setLoantime(String loantime) {
        this.loantime = loantime;
    }

    public String getReturntime() {
        return returntime;
    }

    public void setReturntime(String returntime) {
        this.returntime = returntime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return isbn+"\t"+bookname+"\t"+press+"\t"+
                category+"\t"+updatetime+"\t"+user+"\t"+loantime+
                "\t"+returntime+"\t"+id+"\r\n";
    }

}
