package net.bsoftlab.dao.template.jdbc.mapper;

import net.bsoftlab.model.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleMapper implements RowMapper<Role>{
    @Override
    public Role mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        Role role = new Role();
        role.setID(resultSet.getInt("ID"));
        role.setName(resultSet.getString("Name"));
        return role;
    }
}
