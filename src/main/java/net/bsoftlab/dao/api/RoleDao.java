package net.bsoftlab.dao.api;

import net.bsoftlab.model.Role;

import java.util.List;

public interface RoleDao {
    Role getRole(Integer ID);
    List<Role> getRoleList();
}
