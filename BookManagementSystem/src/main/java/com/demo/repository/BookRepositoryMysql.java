package com.demo.repository;

//import com.demo.model.BookGemfire;
import com.demo.model.BookMysql;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface BookRepositoryMysql extends CrudRepository<BookMysql, Integer> {

//    @Query(value = "SELECT id,name,age,email FROM BookGemfire")
//    Collection<BookGemfire> getAll();

    @Query(value="select count(id) from BookMysql")
    Integer countAll();

    @Query(value = "SELECT * FROM book limit ?1,?2  ",nativeQuery = true)
    Collection<BookMysql> describe(int start, int num);

    @Query(value = "SELECT *  FROM book  where user = '' and isbn=?1",nativeQuery = true)
    Collection<BookMysql> serachNoloanedBookByIsbn(String isbn);

    @Modifying
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    @Query(value = "update book set user=?1,loantime=?2 where id =?3",nativeQuery = true)
    Integer loanBook(String user,String datetime,Long id);

    @Query(value = "select * from book where id=?1",nativeQuery = true)
    Collection<BookMysql> selectById(Long id);

    @Query(value = "select * from book where isbn=?1",nativeQuery = true)
    Collection<BookMysql> selectByIsbn(String isbn);

    @Query(value = "SELECT *  FROM book  where user = ?1 and isbn=?2",nativeQuery = true)
    Collection<BookMysql> serachloanedBookByBooknameAndIsbn(String user,String isbn);

    @Modifying
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    @Query(value = "update book set user='',loantime='' where id =?1",nativeQuery = true)
    Integer returnBook(Long id);

    @Modifying
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    @Query(value = "update book set bookname=?1,updatetime=?2 where isbn =?3",nativeQuery = true)
    Integer updateBookname(String newbookname,String updatetime,String isbn);

    @Modifying
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    @Query(value = "update book set press=?1,updatetime=?2 where isbn =?3",nativeQuery = true)
    Integer updatePress(String newpress,String updatetime,String isbn);

    @Modifying
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    @Query(value = "update book set category=?1,updatetime=?2 where isbn =?3",nativeQuery = true)
    Integer updateCategory(String newcategory,String updatetime,String isbn);

    @Modifying
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    @Query(value = "update book set isbn=?1,updatetime=?2 where isbn =?3",nativeQuery = true)
    Integer updateIsbn(String newisbn,String updatetime,String isbn);

    Collection<BookMysql> findByBooknameContains(String bookname);

    Collection<BookMysql> findByUserEquals(String user);

    @Transactional(readOnly = false,rollbackFor = Exception.class)
    Collection<BookMysql> deleteByIsbnEquals(String isbn);





//    @Modifying
//    @Transactional(readOnly = false,rollbackFor = Exception.class)
//    @Query(value = "update BookMysql set email='@' where id=?1")
//    Integer update(Long id);
}
