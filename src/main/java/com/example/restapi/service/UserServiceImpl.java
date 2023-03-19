package com.example.restapi.service;

import com.example.restapi.model.User;
import com.example.restapi.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void create(User newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRepository.save(newUser);
    }

    @Override
    @Transactional
    public void update(Long id, User updateUser) {
        updateUser.setId(id);
       if(updateUser.getPassword().isEmpty()){
           updateUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
       }
        userRepository.save(updateUser);
    }

    @Override
    @Transactional
    public void delete(long id) {

        userRepository.deleteById(id);
    }
}
