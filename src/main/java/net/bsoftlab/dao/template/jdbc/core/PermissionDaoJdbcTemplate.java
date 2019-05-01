package net.bsoftlab.dao.template.jdbc.core;

import net.bsoftlab.dao.api.PermissionDao;
import net.bsoftlab.dao.template.jdbc.mapper.PermissionMapper;
import net.bsoftlab.model.Permission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.util.List;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PermissionDaoJdbcTemplate implements PermissionDao {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public PermissionDaoJdbcTemplate(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @PreDestroy
    public void destroy(){}
    @PostConstruct
    public void init(){}

    @Override
    public Permission getPermission(Integer ID) {
        try {
            String permissionSelectStatementSql =
                    "SELECT refpermissions.ID, " +
                            "refpermissions.Name " +
                            "FROM refpermissions " +
                            "WHERE refpermissions.ID = :idPermissionParameter";
            return this.namedParameterJdbcTemplate
                    .queryForObject(permissionSelectStatementSql,
                            new MapSqlParameterSource("idPermissionParameter", ID),
                            new PermissionMapper());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return null;
        }
    }

    @Override
    public Permission getPermission(String name) {
        try {
            String permissionSelectStatementSql =
                    "SELECT refpermissions.ID, " +
                            "refpermissions.Name " +
                            "FROM refpermissions " +
                            "WHERE refpermissions.Name = :namePermissionParameter";
            return this.namedParameterJdbcTemplate
                    .queryForObject(permissionSelectStatementSql,
                            new MapSqlParameterSource().addValue("namePermissionParameter", name),
                            new PermissionMapper());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return null;
        }
    }

    @Override
    public List<Permission> getPermissionList() {
        String permissionsSelectStatementSql =
                "SELECT refpermissions.ID, " +
                        "refpermissions.Name " +
                        "FROM refpermissions " +
                        "ORDER BY refpermissions.ID ASC";
        List<Permission> permissionList = this.namedParameterJdbcTemplate
                .query(permissionsSelectStatementSql,
                        new EmptySqlParameterSource(),
                        new PermissionMapper());
        if (permissionList == null || permissionList.isEmpty()) {
            return null;
        }
        return permissionList;
    }
}