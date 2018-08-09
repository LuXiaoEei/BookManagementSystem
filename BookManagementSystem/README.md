# BookManagementSystem


### URL API

### localhost:8080:
- Before running the server, please set the **datasource**  in the file named application.properties.the vaild 
 value includes *Gemfire,Mysql,File*. Also you can change your Mysql address and port
in this file. for running the Gemfire correctly, you need run the Gemfire whose port is 10334 before
runing the server.

1. **/add?isbn=&bookname=&press=&category=&id=**
    - the url can be used to add a book info into the specified datasource .
    - Isbn and bookname is required in all datasource, and in mysql and File the id is also required,
    otherwise, it will throw the error.
    - Press,category,updatetime, user, loantime and returntime are not required, their default value are both ""
    - **note:** In Mysql, because of the automatic generation mechanism of primary key (id), even though set the value of id, 
    the id will not work.
    
2. **/delete?isbn=**
    - we only provide the function of deleting the date by isbn. Depend on the ibsn, it will  delete the corresponding rerord.
    - you must input the vaild isbn, otherwise it will throw the error
    
3. **/update?condition=&bookname=&category=&press=&isbn=**
    - The url can be used to update the bookname, category, press, isbn by condition(orign isbn)
    - Condition is the required, it's the value of isbn of the book you want to update
    - Bookname,category,press,isbn is not required, it's the new value of the corresponding field. 
    - If you want to set some fields to empty, you can set the field to **"" or ''**, however, 
    if you set the isbn or bookname to empty, it will throw the error.

4. **/describe?start=&nums=**
    - It can be used to browse the data.
    - It will return the record of the table  where the rownum is between start and start+num in Mysql and File. In Gemfire
    the parameter *start* doesn't work, whatever the value of start you input, it only can return the Top nums date
    - Start and nums are both not required,their default value is 0 and the rows of table respectively.
    
5. **/select/bookname/{bookname}**
    - It can be used to search book depend on the bookname
    - Bookname is required
    - Depend on the parameter bookname to do fuzzy matching and return the matched value
    
6. **/select/user/{user}**
    - Used to find out which books a person has borrowed
    - User is required
    - It will do accurate matching
    

7. **/loadbook?user=&isbn=**
    - the user and isbn are required
    - Implement function which user borrow the book whose isbn is isbn
    - If there is no book named bookname or all books named bookname have been borrowed ,it will return the prompt "no book remained"
    - Otherwise, it will update the user and lendtime fields in table. And return the prompt "user success borrowing isbn: isbn at loantime"
        1. In Mysql, if the user of the book is empty, we regard the book as a book that has not been borrowed.
        2. In Gemfire and File, if the loadtime is empyt or the loadtime is less than the returntime, we regard the book as a book that has not been borrowed.
    
8. **/returnbook?user=&isbn=**
    - the user and isbn are required.
    - Implement function which user return the book whose isbn is isbn
    - If user doesn't borrow the book,it will return the prompt "you have not borrowed the book"
    - Else, In the Mysql, it will set the user and lendtime to "" and return te prompt "user success returning isbn:isbn at now time)".
    In Gemfire and File, it will directly update the returntime
    