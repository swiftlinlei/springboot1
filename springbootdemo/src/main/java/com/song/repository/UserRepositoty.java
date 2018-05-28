package com.song.repository;

import com.song.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by linlei on 2018/5/26
 * 员工表操作接口
 */
public interface UserRepositoty extends JpaRepository<User,String> {

    @Query("select t from User t where t.id = :id")
    User findByUserId(@Param("id") String id);
}
