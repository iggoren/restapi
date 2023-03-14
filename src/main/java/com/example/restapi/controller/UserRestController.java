package com.example.restapi.controller;

import com.example.restapi.model.User;
import com.example.restapi.service.RoleService;
import com.example.restapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/admin")
public class UserRestController {

    private final UserService userService;
    private final RoleService roleService;

    public UserRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userServiceAll = userService.findAll();
        return !userServiceAll.isEmpty()
                ? new ResponseEntity<>(userServiceAll, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @GetMapping("/new")
//    public ResponseEntity getUser(@ModelAttribute("user") User user, Model model) {
//        model.addAttribute("roles", roleService.listRoles());
//        return "new";
//    }

}
