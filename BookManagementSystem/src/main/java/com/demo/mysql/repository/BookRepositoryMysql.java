package com.demo.mysql.repository;

//import com.demo.geode.model.BookGemfire;
import com.demo.mysql.model.BookMysql;
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

    @Query(value = "SELECT * FROM book limit ?1  ",nativeQuery = true)
    Collection<BookMysql> describle(int end);

    @Query(value = "SELECT *  FROM book  where user = '' and bookname=?1",nativeQuery = true)
    Collection<BookMysql> serachNolendedBookByBookname(String bookname);

    @Modifying
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    @Query(value = "update book set user=?1,lendtime=?2 where id =?3",nativeQuery = true)
    Integer lendBook(String user,String datetime,Long id);

    @Query(value = "select * from book where id=?1",nativeQuery = true)
    Collection<BookMysql> selectById(Long id);

    @Query(value = "SELECT *  FROM book  where user = ?1 and bookname=?2",nativeQuery = true)
    Collection<BookMysql> serachlendedBookByBooknameAndUser(String user,String bookname);

    @Modifying
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    @Query(value = "update book set user='',lendtime='' where id =?1",nativeQuery = true)
    Integer returnBook(Long id);



//    @Modifying
//    @Transactional(readOnly = false,rollbackFor = Exception.class)
//    @Query(value = "update BookMysql set email='@' where id=?1")
//    Integer update(Long id);
}
