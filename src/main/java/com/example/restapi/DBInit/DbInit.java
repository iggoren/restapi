package com.example.restapi.DBInit;

import com.example.restapi.model.Role;
import com.example.restapi.model.User;
import com.example.restapi.service.RoleService;
import com.example.restapi.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.Set;

@Component
public class DbInit {

    private final UserService userService;
    private final RoleService roleService;


    public DbInit(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;

    }

    @PostConstruct
    void createAdmin() {
        Role roleAdmin = roleService.findByRole("ROLE_ADMIN");
        if (roleAdmin == null) {
            roleAdmin = new Role("ROLE_ADMIN");
            roleService.addRole(roleAdmin);
        }
        Role roleUser = roleService.findByRole("ROLE_USER");
        if (roleUser == null) {
            roleUser = new Role("ROLE_USER");
            roleService.addRole(roleUser);
        }

        Optional<User> admin = userService.findByEmail("admin");
        Optional<User> user = userService.findByEmail("user");


        if (admin.isEmpty()) {
            userService.create(new User("admin", "admin", 25, Set.of(roleAdmin,roleUser)));
        }
        if (user.isEmpty()) {
            userService.create(new User("user", "user", 25, Set.of(roleUser)));
        }

    }


}
