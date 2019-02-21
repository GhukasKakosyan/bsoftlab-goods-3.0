package net.bsoftlab.dao.template.jdbc.mapper;

import net.bsoftlab.model.Address;
import net.bsoftlab.model.Department;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DepartmentMapper implements RowMapper<Department> {
    @Override
    public Department mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        Department department = new Department();
        department.setCode(resultSet.getString("Code"));
        department.setName(resultSet.getString("Name"));
        department.setAddress(new Address());
        department.getAddress().setStreet(resultSet.getString("Street"));
        department.getAddress().setPincode(resultSet.getString("Pincode"));
        department.getAddress().setCity(resultSet.getString("City"));
        department.getAddress().setState(resultSet.getString("State"));
        department.getAddress().setCountry(resultSet.getString("Country"));
        department.setPhones(resultSet.getString("Phones"));
        department.setFaxes(resultSet.getString("Faxes"));
        department.setWebSite(resultSet.getString("WebSite"));
        department.setEmailAddress(resultSet.getString("EmailAddress"));
        department.setAdditionalInformation(resultSet.getString("AdditionalInformation"));
        return department;
    }
}
