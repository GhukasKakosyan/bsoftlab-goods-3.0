package net.bsoftlab.dao.template.jdbc.core;

import net.bsoftlab.dao.DepartmentDao;
import net.bsoftlab.dao.template.jdbc.mapper.DepartmentMapper;
import net.bsoftlab.model.Department;
import net.bsoftlab.utility.UtilityFunctions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.util.List;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DepartmentDaoJdbcTemplate implements DepartmentDao {

    private JdbcTemplate jdbcTemplate;
    private UtilityFunctions utilityFunctions = null;

    @Autowired
    public DepartmentDaoJdbcTemplate(
            JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PreDestroy
    public void destroy() {}
    @PostConstruct
    public void init() {}

    @Autowired
    public void setUtilityFunctions(
            UtilityFunctions utilityFunctions) {
        this.utilityFunctions = utilityFunctions;
    }

    @Override
    public void deleteDepartment(Department department) {
        String departmentDeleteStatementSQL =
                "DELETE FROM refdepartments " +
                        "WHERE refdepartments.Code = ?";
        this.jdbcTemplate.update(departmentDeleteStatementSQL, department.getCode());
    }

    @Override
    public void insertDepartment(Department department) {
        String departmentInsertStatementSql =
                "INSERT INTO refdepartments " +
                        "(Code, Name, Street, Pincode, City, State, Country, " +
                        "Phones, Faxes, WebSite, EmailAddress, AdditionalInformation) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        this.jdbcTemplate.update(departmentInsertStatementSql,
                department.getCode(),
                department.getName(),
                department.getAddress().getStreet(),
                department.getAddress().getPincode(),
                department.getAddress().getCity(),
                department.getAddress().getState(),
                department.getAddress().getCountry(),
                department.getPhones(),
                department.getFaxes(),
                department.getWebSite(),
                department.getEmailAddress(),
                department.getAdditionalInformation());
    }

    @Override
    public void updateDepartment(Department department) {
        String departmentUpdateStatementSql =
                "UPDATE refdepartments " +
                        "SET refdepartments.Name = ?, " +
                        "refdepartments.Street = ?, " +
                        "refdepartments.Pincode = ?, " +
                        "refdepartments.City = ?, " +
                        "refdepartments.State = ?, " +
                        "refdepartments.Country = ?, " +
                        "refdepartments.Phones = ?, " +
                        "refdepartments.Faxes = ?, " +
                        "refdepartments.WebSite = ?, " +
                        "refdepartments.EmailAddress = ?, " +
                        "refdepartments.AdditionalInformation = ? " +
                        "WHERE refdepartments.Code = ?";
        this.jdbcTemplate.update(departmentUpdateStatementSql,
                department.getName(),
                department.getAddress().getStreet(),
                department.getAddress().getPincode(),
                department.getAddress().getCity(),
                department.getAddress().getState(),
                department.getAddress().getCountry(),
                department.getPhones(),
                department.getFaxes(),
                department.getWebSite(),
                department.getEmailAddress(),
                department.getAdditionalInformation(),
                department.getCode());
    }

    @Override
    public Department getDepartment(String code) {
        try {
            String departmentSelectStatementSQL =
                    "SELECT refdepartments.Code, " +
                            "refdepartments.Name, " +
                            "refdepartments.Street, " +
                            "refdepartments.Pincode, " +
                            "refdepartments.City, " +
                            "refdepartments.State, " +
                            "refdepartments.Country, " +
                            "refdepartments.Phones, " +
                            "refdepartments.Faxes, " +
                            "refdepartments.WebSite, " +
                            "refdepartments.EmailAddress, " +
                            "refdepartments.AdditionalInformation " +
                            "FROM refdepartments " +
                            "WHERE refdepartments.Code = ?";
            return this.jdbcTemplate.queryForObject(
                    departmentSelectStatementSQL,
                    new Object[]{code}, new DepartmentMapper());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return null;
        }
    }

    @Override
    public List<Department> getDepartmentList(int start, int size) {
        String departmentListSelectStatementSql =
                "SELECT refdepartments.Code, " +
                        "refdepartments.Name, " +
                        "refdepartments.Street, " +
                        "refdepartments.Pincode, " +
                        "refdepartments.City, " +
                        "refdepartments.State, " +
                        "refdepartments.Country, " +
                        "refdepartments.Phones, " +
                        "refdepartments.Faxes, " +
                        "refdepartments.WebSite, " +
                        "refdepartments.EmailAddress, " +
                        "refdepartments.AdditionalInformation " +
                        "FROM refdepartments " +
                        "ORDER BY refdepartments.Code ASC";
        List<Department> departmentList = this.jdbcTemplate.query(
                departmentListSelectStatementSql,
                new DepartmentMapper());
        if(departmentList == null || departmentList.isEmpty()) {
            return null;
        }

        int first = this.utilityFunctions.calculateFirst(start, size, departmentList.size());
        int quantity = this.utilityFunctions.calculateQuantity(start, size, departmentList.size());
        List<Department> departmentSubList = departmentList.subList(first, quantity);
        if(departmentSubList.isEmpty()) {
            return null;
        }
        return departmentSubList;
    }
}