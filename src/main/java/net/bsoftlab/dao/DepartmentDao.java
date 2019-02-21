package net.bsoftlab.dao;

import net.bsoftlab.model.Department;

import java.util.List;

public interface DepartmentDao {
    void deleteDepartment(Department department);
    void insertDepartment(Department department);
    void updateDepartment(Department department);
    Department getDepartment(String code);
    List<Department> getDepartmentList(int start, int size);
}