//package com.example.restapi.controller;
//
//import com.example.restapi.model.User;
//import com.example.restapi.service.RoleService;
//import com.example.restapi.service.UserService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.security.Principal;
//import java.util.Optional;
//
//@Controller
//@RequestMapping("/user")
//public class UserController {
//
//    private final UserService userService;
//    private final RoleService roleService;
//
//    public UserController(UserService userService, RoleService roleService) {
//        this.userService = userService;
//        this.roleService = roleService;
//    }
//
//    @GetMapping
//    public String findAll(Model model, Principal principal) {
//        Optional<User> user = userService.findByEmail(principal.getName());
//
//        model.addAttribute("user", user.orElseThrow(()->new UsernameNotFoundException("user не найден")));
//        return "user/users";
//    }
//
//    @GetMapping("/{id}")
//    public String findById(@PathVariable("id") Long id, Model model) {
//        model.addAttribute("user", userService.findById(id).orElseThrow());
//        model.addAttribute("roles", roleService.listRoles());
//        return "user/edit";
//    }
//}
