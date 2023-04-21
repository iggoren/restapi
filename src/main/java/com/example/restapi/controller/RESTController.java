package com.example.restapi.controller;

import com.example.restapi.model.Role;
import com.example.restapi.model.User;
import com.example.restapi.service.RoleService;
import com.example.restapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping
@CrossOrigin
public class RESTController {

    private final UserService userService;
    private final RoleService roleService;
    @Autowired
    public RESTController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<User>> printUsers() {
        final List<User> users = userService.findAll();

        return users != null &&  !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<User> printOneUser(@PathVariable("id") Long id) {
        final User user = userService.findById(id).get();

        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/admin/all")
    public ResponseEntity<User> addNewUser(@RequestBody User user) {
        userService.create(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PatchMapping("/admin/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody User user) {
        userService.update(id, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/admin/del/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

//    @GetMapping("/user/auth")
//    public ResponseEntity<User> printAuthUser(Principal principal) {
//        User userByName = userService.findUserByName(principal.getName());
// //
//        return new ResponseEntity<>(userByName, HttpStatus.OK);
//    }

    @GetMapping("admin/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return new ResponseEntity<>(roleService.listRoles(), HttpStatus.OK);
    }
}
