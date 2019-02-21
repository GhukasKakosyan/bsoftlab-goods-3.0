package net.bsoftlab.resource.container;

import net.bsoftlab.resource.DepartmentResource;

import java.util.List;

public class DepartmentResourceListContainer {
    private List<DepartmentResource> departmentResourceList = null;

    public List<DepartmentResource> getDepartmentResourceList() {
        return this.departmentResourceList;
    }
    public void setDepartmentResourceList(
            List<DepartmentResource> departmentResourceList) {
        this.departmentResourceList = departmentResourceList;
    }
}
