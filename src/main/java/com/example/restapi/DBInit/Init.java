package com.example.restapi.DBInit;//package ru.web.Pre_3_1_2_sb263.zinit;
//
import com.example.restapi.model.Role;
import com.example.restapi.model.User;
import com.example.restapi.service.RoleService;
import com.example.restapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;


import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class Init {
    private final UserService userService;
    private final RoleService roleService;
    @Autowired
    public Init(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void init() {
        Role role1 = new Role("ROLE_ADMIN");
        Role role2 = new Role("ROLE_USER");

        roleService.addRole(role1);
        roleService.addRole(role2);

        List<Role> roleAdmin = new ArrayList<>();
        List<Role> roleUser = new ArrayList<>();
        List<Role> roleAll = new ArrayList<>();

        roleAdmin.add(role1);
        roleUser.add(role2);
        roleAll.add(role1);
        roleAll.add(role2);

        User admin = new User("admin", "adminov", "admin@mail.ru", (byte) 50, "admin", roleAdmin);
        User user1 = new User("user", "userov", "user@mail.ru", (byte) 15, "user", roleUser);
        User user2 = new User("user2", "userov2", "user2@mail.ru", (byte) 120, "user2", roleAll);

        userService.create(admin);
        userService.create(user1);
        userService.create(user2);
    }
}