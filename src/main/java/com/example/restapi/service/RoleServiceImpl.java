package com.example.restapi.service;

import com.example.restapi.model.Role;
import com.example.restapi.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByRole(String role) {
        return roleRepository.findByRole(role);
    }

    @Override
    public void addRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    public List<Role> listRoles() {
        return roleRepository.findAll();
    }
}
