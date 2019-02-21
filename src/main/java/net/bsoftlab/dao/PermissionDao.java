package net.bsoftlab.dao;

import net.bsoftlab.model.Permission;

import java.util.List;

public interface PermissionDao {
    Permission getPermission(Integer ID);
    Permission getPermission(String name);
    List<Permission> getPermissionList();
}
