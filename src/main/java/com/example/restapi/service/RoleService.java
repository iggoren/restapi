package com.example.restapi.service;

import com.example.restapi.model.Role;

import java.util.List;

public interface RoleService {
    Role findByRole(String role);
    void  addRole(Role role);
    List<Role> listRoles();
}
