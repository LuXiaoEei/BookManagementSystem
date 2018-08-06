package com.demo.mysql.repository;

//import com.demo.geode.model.BookGemfire;
import com.demo.mysql.model.BookMysql;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface BookRepositoryMysql extends CrudRepository<BookMysql, Integer> {

//    @Query(value = "SELECT id,name,age,email FROM BookGemfire")
//    Collection<BookGemfire> getAll();

    @Query(value="select count(id) from BookMysql")
    Integer countAll();

    @Query(value = "SELECT * FROM book limit ?1  ",nativeQuery = true)
    Collection<BookMysql> describle(int rows);

//    @Modifying
//    @Transactional(readOnly = false,rollbackFor = Exception.class)
//    @Query(value = "update BookMysql set email='@' where id=?1")
//    Integer update(Long id);
}
