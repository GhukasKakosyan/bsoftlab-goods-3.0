package net.bsoftlab.service;

import net.bsoftlab.model.Department;

import java.util.List;

public interface DepartmentService {
    void deleteDepartment(String code);
    void insertDepartment(Department department);
    void updateDepartment(Department department);
    Department getDepartment(String code);
    List<Department> getDepartmentList();
    List<Department> getDepartmentList(int start, int size);
}