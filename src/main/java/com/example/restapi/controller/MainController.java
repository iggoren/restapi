package com.example.restapi.controller;

import com.example.restapi.model.User;
import com.example.restapi.service.RoleService;
import com.example.restapi.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class MainController {

    private UserService userService;
    private final RoleService roleService;

    public MainController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

        @GetMapping()
    public String findAll(@ModelAttribute("user") User user, Model model, Principal principal) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", roleService.listRoles());
        model.addAttribute("principal", userService.findByEmail(principal.getName()).get());
        return "admin";
    }


    @GetMapping("/new")
    public String getUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", roleService.listRoles());
        return "new";
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("roles", roleService.listRoles());
        return "user/edit";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.update(user);
        return "redirect:/admin";

    }

    @PostMapping()
    public String addUser(@ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/admin/new";
        }
        userService.create(user);
        return "redirect:admin";
    }

    @GetMapping("/index")
    public String showUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println(userDetails);
        return "index";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
