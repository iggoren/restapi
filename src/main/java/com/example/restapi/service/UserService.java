package com.example.restapi.service;

import com.example.restapi.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();
      Optional<User> findById(long id);
      Optional<User> findByEmail(String email);

    void create(User user);
    void update(User user);
    void delete(long id);



}
