package net.bsoftlab.service;

import net.bsoftlab.model.Permission;

import java.util.List;

public interface PermissionService {
    Permission getPermission(Integer ID);
    Permission getPermission(String name);
    List<Permission> getPermissionList();
}
