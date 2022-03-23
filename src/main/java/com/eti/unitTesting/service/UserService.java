package com.eti.unitTesting.service;

import com.eti.unitTesting.entity.User;

import java.util.List;

public interface UserService {

    User save(User user);

    User update(User user);

    void deleteById(Long id) throws Exception;

    User findById(Long id) throws Exception;

    List<User> findAll();

    User findByEmailOrLogin(String searchString);
}
