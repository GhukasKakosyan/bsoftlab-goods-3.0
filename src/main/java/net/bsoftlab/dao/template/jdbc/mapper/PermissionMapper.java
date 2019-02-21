package net.bsoftlab.dao.template.jdbc.mapper;

import net.bsoftlab.model.Permission;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PermissionMapper implements RowMapper<Permission>{
    @Override
    public Permission mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        Permission permission = new Permission();
        permission.setID(resultSet.getInt("ID"));
        permission.setName(resultSet.getString("Name"));
        return permission;
    }
}
