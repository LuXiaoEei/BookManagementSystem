# BookManagementSystem

### BookEntry

- id: *String*, primary key ,it can be generated automaticallythe by uuid, and needn't be set.
- bookname: *String*, the name of the book, it can't be empty;
- isbn: *String*, the ISBN or ASBN of the book, the length of this field in the datasource must be 13,
and the length of the vaild input must be 10 or 13, which only contains numbers or letters. If the length is 13,
it has to start with  **978**;  
- press: *String*, the publisher of the book;
- category: *String*, the category of the book, such as science or life......
- user: *String*, the person who borrows and returns books from the datasource;
- loantime: *String*, in Mysql, it is the time to borrow the books;in Gemfire and File, if nobody borrows books, 
it is the time of the last person to borrow books,and if someone borrow the book now,it is just the time of the person now.   
- returntime: *String*, it is the time to return the books;
- updatetime: *String*, it is the time to save the book or change the information of the book.

### URL API

### localhost:8080
- Before running the server, please set the value of  **datasource**  in the application.properties.the vaild 
 value includes *Gemfire,Mysql,File*. You can also change your Mysql address and port in this file.In order to run the Gemfire 
 correctly, you need run the Gemfire whose port is 10334 and create regin named BookGemfire before runing the server.

1. **/add?isbn=&bookname=&press=&category=**
    - the url can be used to add a book information by the specified datasource.
    - Isbn and bookname is required in all datasource. 
    - Press,category,updatetime, user, loantime and returntime are not required, their default value is ""
    - it also support post
    
2. **/delete?isbn=**
    - we only provide the function of deleting the data by isbn. Depend on the ibsn, it will  delete the corresponding rerord.
    - you must input the vaild isbn, otherwise it will throw the error
    
3. **/update?condition=&bookname=&category=&press=&isbn=**
    - The url can be used to update the bookname, category, press, isbn by condition(orign isbn)
    - Condition is required, which's the value of isbn of the book you want to update
    - Bookname,category,press,isbn is not required, it's the new value of the corresponding field. 
    - If you want to set some fields to empty, you can set the field to **"" or ''**, however, 
    if you set the isbn or bookname to empty, it will throw the error.
    
4. **/info**
    - don't need any parameter
    - It will return the datasouece and the amount of data in current database; 

5. **/describe?start=&nums=**
    - It can be used to browse the data.
    - It will return the record of the table  where the rownum is between start and start + num in Mysql and File. In Gemfire
    the parameter *start* doesn't work, whatever the value of start you input, it only can return the Top nums data
    - start and nums are both not required,their default value is 0 and the all rows of the table respectively.
    - if you set the Start and nums to invaild such as negative or String, it will throw the error
    - index starts from 0
    
6. **/select/bookname/{bookname}**
    - It can be used to search book depend on the bookname
    - Bookname is required
    - Depend on the parameter of bookname to do fuzzy matching and return the matched value
    
7. **/select/user/{user}**
    - It can be used to find out person who borrowed the books from the datasoure
    - User is required
    - It will do accurate matching    

8. **/loanbook?user=&isbn=**
    - the user and isbn are required
    - Implement function which user borrows the book by isbn
    - If there is no book named bookname or all books named bookname have been borrowed ,it will return the prompt "no book remained"
    - Otherwise, it will update the user and loantime fields in table. And return the prompt "user success borrowing isbn: isbn at loantime"
        1. In Mysql, if the user of the book is empty, we think the book that has not been borrowed.
        2. In Gemfire and File, if the loadtime is empty or the loadtime is less than the returntime, we think the book that has not been borrowed.

9. **/returnbook?user=&isbn=**
    - the user and isbn are required.
    - Implement function which user return the book by isbn
    - If user doesn't borrow the book,it will return the prompt "you have not borrowed the book"
    - In the Mysql, it will set the user and lendtime to "" and return the prompt "user success returning isbn:isbn at now time)".
    In Gemfire and File, it will directly update the returntime.
    
