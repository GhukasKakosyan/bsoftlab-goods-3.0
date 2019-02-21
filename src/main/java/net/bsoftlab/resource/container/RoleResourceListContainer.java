package net.bsoftlab.resource.container;

import net.bsoftlab.resource.RoleResource;

import java.util.List;

public class RoleResourceListContainer {
    private List<RoleResource> roleResourceList = null;

    public List<RoleResource> getRoleResourceList() {
        return this.roleResourceList;
    }
    public void setRoleResourceList(
            List<RoleResource> roleResourceList) {
        this.roleResourceList = roleResourceList;
    }
}
