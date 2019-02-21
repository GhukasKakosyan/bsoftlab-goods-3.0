package net.bsoftlab.dao.template.jdbc.mapper;

import net.bsoftlab.model.Group;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupMapper implements RowMapper<Group> {
    @Override
    public Group mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        Group group = new Group();
        group.setCode(resultSet.getString("Code"));
        group.setName(resultSet.getString("Name"));
        group.setEnhancedName(resultSet.getString("EnhancedName"));
        return group;
    }
}
