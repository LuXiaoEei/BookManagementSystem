# BookManagementSystem


### URL API

### localhost:8080/2/ : 
- If the url begin with the /2 after 8080, it mean the following operation will be done in the Mysql.


1. **/add?isbn=&bookname=&press=&category=**
    - Add book
    - Isbn,bookname is required
    - Press,category is not required , their default value are both ""
   
2. **/loadbook?user=&isbn=**
    - Implement function which user borrow the book named bookname
    - If there is no book named bookname or all books have  been borrowed ,it will return the prompt 
    - Else, it will update the user and lendtime fields in table. And return the prompt 
    
3. **/returnbook?user=&isbn=**
    - Implement function which user return the book named bookname
    - If user doesn't borrow the book,it will return the prompt .
    - Else ,it will set the user and lendtime to "" and return te prompt .
    
4. **/update?condition=&bookname=&category=&press=&isbn=**
    - Condition is the required, it's th value of isbn where want to update
    - Bookname,category,press,isbn is not required, it's the new value of the corresponding field.
    - If want to set one field null, you can set the field to "" or '' , but it's forbidden to do set the isbn to null, if you try to do this, it will throw error. 

5. **/describe?start=&nums=**
    - Return the record of the table the where the rownum is between start and start+num.
    - Start and nums are not  both the required,their default value is 0 and the rows of table.

6. **/select/bookname/{bookname}**
    - Used to search book depend on the bookname
    - Bookname is required
    - Depend on the parameter bookname to do fuzzy matching 
    
7. **/select/user/{user}**
    - Used to find the book which the user is borrowing
    - User is required
    - It will do accurate matching
    
8. **/delete?isbn=**
    - isbn is required.
    - Depend on the ibsn, it will  delete the corresponding rerord.
    
