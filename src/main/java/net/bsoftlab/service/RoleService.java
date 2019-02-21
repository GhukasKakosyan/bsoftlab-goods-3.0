package net.bsoftlab.service;

import net.bsoftlab.model.Role;

import java.util.List;

public interface RoleService {
    Role getRole(Integer ID);
    List<Role> getRoleList();
}
