package com.eti.unitTesting.repository;

import com.eti.unitTesting.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select u " +
            "from User u " +
            "where u.email =:emailOrLogin " +
            "or u.login =:emailOrLogin")
    User findAllByEmailOrLogin(String emailOrLogin);

    @Query(value = "select CASE  WHEN count(u)> 0 THEN true ELSE false END " +
            "from User u " +
            "where u.login =:login " +
            "or u.login =:login")
    Boolean existsByLogin(String login);

    @Query(value = "select CASE  WHEN count(u)> 0 THEN true ELSE false END " +
            "from User u " +
            "where u.email =:email")
    Boolean existsByEmail(String email);
}
