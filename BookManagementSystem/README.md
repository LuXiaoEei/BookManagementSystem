# BookManagementSystem

### BookEntry

- id: *String*, primary key ,it can't be set to empty, the vaild id must
 only contain numbers;
- bookname: *String*, the name of the book, it can't be empty;
- isbn: *String*, the ISBN or ASBN of the book, the length of this field in the datasource must be 13,
and the length of vaild input value must be 10 or 13, and only contain numbers and letters. If the length is 13,
the first three characters must be **978**.  
- press: *String*, the publishing company of the book,
- category: *String*, the category of the book, such as science or life...
- user: *String*, the name of person who is borrowing the book now.
- loantime: *String*, in Mysql, it is the time to borrow the books;Im Gemfire and File, if some one borrow the 
book,it is also the time to borrow the books. If no one borrow the book, it is the time of the last 
person to borrow books.
- returntime: *String*, in Mysql, it is the time to return the books;Im Gemfire and File, it is the time to return the  books.
- updatetime: *String*, it is the time to save the book or the time to change the basic info of the book.

### URL API

### localhost:8080
- Before running the server, please set the value of  **datasource**  in the file named application.properties.the vaild 
 value includes *Gemfire,Mysql,File*. Also you can change your Mysql address and port in this file. for running the Gemfire 
 correctly, you need run the Gemfire whose port is 10334 and create regin named BookGemfire before runing the server.

1. **/add?isbn=&bookname=&press=&category=&id=**
    - the url can be used to add a book info into the specified datasource .
    - Isbn and bookname is required in all datasource, and in mysql and File the id is also required,
    otherwise, it will throw the error.
    - Press,category,updatetime, user, loantime and returntime are not required, their default value are both ""
    - **note:** In Mysql, because of the automatic generation mechanism of primary key (id), even though set the value of id, 
    the id will not work.
    - it also support post
    
2. **/delete?isbn=**
    - we only provide the function of deleting the date by isbn. Depend on the ibsn, it will  delete the corresponding rerord.
    - you must input the vaild isbn, otherwise it will throw the error
    
3. **/update?condition=&bookname=&category=&press=&isbn=**
    - The url can be used to update the bookname, category, press, isbn by condition(orign isbn)
    - Condition is the required, it's the value of isbn of the book you want to update
    - Bookname,category,press,isbn is not required, it's the new value of the corresponding field. 
    - If you want to set some fields to empty, you can set the field to **"" or ''**, however, 
    if you set the isbn or bookname to empty, it will throw the error.
    
4. **/info**
    - don't need any parameter
    - It will return the datasouece used now and the amount of data in current database; 

5. **/describe?start=&nums=**
    - It can be used to browse the data.
    - It will return the record of the table  where the rownum is between start and start+num in Mysql and File. In Gemfire
    the parameter *start* doesn't work, whatever the value of start you input, it only can return the Top nums date
    - start and nums are both not required,their default value is 0 and the rows of table respectively.
    - if you set the Start and nums to invaild such as negative or String, it will trow the error
    - index starts from 0
    
6. **/select/bookname/{bookname}**
    - It can be used to search book depend on the bookname
    - Bookname is required
    - Depend on the parameter bookname to do fuzzy matching and return the matched value
    
7. **/select/user/{user}**
    - Used to find out which books a person has borrowed
    - User is required
    - It will do accurate matching    

8. **/loadbook?user=&isbn=**
    - the user and isbn are required
    - Implement function which user borrow the book whose isbn is isbn
    - If there is no book named bookname or all books named bookname have been borrowed ,it will return the prompt "no book remained"
    - Otherwise, it will update the user and lendtime fields in table. And return the prompt "user success borrowing isbn: isbn at loantime"
        1. In Mysql, if the user of the book is empty, we regard the book as a book that has not been borrowed.
        2. In Gemfire and File, if the loadtime is empyt or the loadtime is less than the returntime, we regard the book as a book that has not been borrowed.

9. **/returnbook?user=&isbn=**
    - the user and isbn are required.
    - Implement function which user return the book whose isbn is isbn
    - If user doesn't borrow the book,it will return the prompt "you have not borrowed the book"
    - Else, In the Mysql, it will set the user and lendtime to "" and return te prompt "user success returning isbn:isbn at now time)".
    In Gemfire and File, it will directly update the returntime.
    
