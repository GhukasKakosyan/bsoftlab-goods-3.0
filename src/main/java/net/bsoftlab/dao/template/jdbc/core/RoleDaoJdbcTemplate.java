package net.bsoftlab.dao.template.jdbc.core;

import net.bsoftlab.dao.RoleDao;
import net.bsoftlab.dao.template.jdbc.mapper.PermissionMapper;
import net.bsoftlab.dao.template.jdbc.mapper.RoleMapper;
import net.bsoftlab.dao.template.jdbc.mapper.WorkmanMapper;

import net.bsoftlab.model.Address;
import net.bsoftlab.model.Permission;
import net.bsoftlab.model.Role;
import net.bsoftlab.model.Workman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.util.HashSet;
import java.util.List;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RoleDaoJdbcTemplate implements RoleDao {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public RoleDaoJdbcTemplate(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @PreDestroy
    public void destroy(){}
    @PostConstruct
    public void init(){}

    @Override
    public Role getRole(Integer ID) {

        Role role;
        try {
            String roleSelectStatementSql =
                    "SELECT refroles.ID, " +
                            "refroles.Name " +
                            "FROM refroles " +
                            "WHERE refroles.ID = :idRoleParameter";
            role = this.namedParameterJdbcTemplate.queryForObject(
                    roleSelectStatementSql,
                    new MapSqlParameterSource("idRoleParameter", ID),
                    new RoleMapper());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return null;
        }

        String permissionsRoleSelectStatementSql =
                "SELECT refpermissions.ID, " +
                        "refpermissions.Name " +
                        "FROM refpermissions, refrolespermissions " +
                        "WHERE refrolespermissions.PermissionID = refpermissions.ID " +
                        "AND refrolespermissions.RoleID = :idRoleParameter " +
                        "ORDER BY refpermissions.ID";
        List<Permission> permissionListRole = this.namedParameterJdbcTemplate.query(
                permissionsRoleSelectStatementSql,
                new MapSqlParameterSource("idRoleParameter", ID),
                new PermissionMapper());
        if (permissionListRole != null && !permissionListRole.isEmpty()) {
            role.setPermissions(new HashSet<>());
            role.getPermissions().addAll(permissionListRole);
        }

        String workmansRoleSelectStatementSql =
                "SELECT refworkmans.ID, " +
                        "refworkmans.Name, " +
                        "refworkmans.Password, " +
                        "refworkmans.FirstName, " +
                        "refworkmans.LastName, " +
                        "refworkmans.Phones, " +
                        "refworkmans.Street, " +
                        "refworkmans.Pincode, " +
                        "refworkmans.City, " +
                        "refworkmans.State, " +
                        "refworkmans.Country, " +
                        "refroles.ID AS RoleID, " +
                        "refroles.Name AS RoleName " +
                        "FROM refroles, refworkmans, refworkmansroles " +
                        "WHERE refworkmansroles.WorkmanID = refworkmans.ID " +
                        "AND refworkmansroles.RoleID = refroles.ID " +
                        "AND refworkmansroles.RoleID = :idRoleParameter " +
                        "ORDER BY refworkmans.ID ASC";
        List<Workman> workmanListRole = this.namedParameterJdbcTemplate.query(
                workmansRoleSelectStatementSql,
                new MapSqlParameterSource("idRoleParameter", ID),
                new WorkmanMapper());
        if (workmanListRole != null && !workmanListRole.isEmpty()) {
            role.setWorkmans(new HashSet<>());
            role.getWorkmans().addAll(workmanListRole);
        }
        return role;
    }

    @Override
    public List<Role> getRoleList() {
        String rolesSelectStatementSql =
                "SELECT refroles.ID, " +
                        "refroles.Name " +
                        "FROM refroles " +
                        "ORDER BY refroles.ID ASC";
        List<Role> roleList = this.namedParameterJdbcTemplate.query(
                rolesSelectStatementSql, new EmptySqlParameterSource(), new RoleMapper());
        if (roleList == null || roleList.isEmpty()) {
            return null;
        }

        String permissionsRolesSelectStatementSql =
                "SELECT refrolespermissions.RoleID, " +
                        "refpermissions.ID AS PermissionID, " +
                        "refpermissions.Name AS PermissionName " +
                        "FROM refpermissions, refrolespermissions " +
                        "WHERE refpermissions.ID = refrolespermissions.PermissionID " +
                        "ORDER BY refrolespermissions.RoleID ASC, " +
                        "refrolespermissions.PermissionID ASC, refrolespermissions.ID ASC";
        SqlRowSet permissionsRolesSqlRowSet = this.namedParameterJdbcTemplate
                .queryForRowSet(permissionsRolesSelectStatementSql,
                        new EmptySqlParameterSource());

        if (permissionsRolesSqlRowSet != null) {
            for (Role role : roleList) {
                while (permissionsRolesSqlRowSet.next()) {
                    if (role.getID().equals(permissionsRolesSqlRowSet.getInt("RoleID"))) {
                        Permission permission = new Permission();
                        permission.setID(permissionsRolesSqlRowSet.getInt("PermissionID"));
                        permission.setName(permissionsRolesSqlRowSet.getString("PermissionName"));
                        role.getPermissions().add(permission);
                    }
                }
                permissionsRolesSqlRowSet.beforeFirst();
            }
        }

        String workmansRolesSelectStatementSql =
                "SELECT refroles.ID AS RoleID, " +
                        "refworkmans.ID, " +
                        "refworkmans.Name, " +
                        "refworkmans.Password, " +
                        "refworkmans.FirstName, " +
                        "refworkmans.LastName, " +
                        "refworkmans.Phones, " +
                        "refworkmans.Street, " +
                        "refworkmans.Pincode, " +
                        "refworkmans.City, " +
                        "refworkmans.State, " +
                        "refworkmans.Country " +
                        "FROM refroles, refworkmans, refworkmansroles " +
                        "WHERE refworkmansroles.WorkmanID = refworkmans.ID " +
                        "AND refworkmansroles.RoleID = refroles.ID " +
                        "ORDER BY refroles.ID ASC, refworkmans.ID ASC";
        SqlRowSet workmansRolesSqlRowSet = this.namedParameterJdbcTemplate.queryForRowSet(
                workmansRolesSelectStatementSql, new EmptySqlParameterSource());

        if (workmansRolesSqlRowSet != null) {
            for (Role role : roleList) {
                while (workmansRolesSqlRowSet.next()) {
                    if (role.getID().equals(workmansRolesSqlRowSet.getInt("RoleID"))) {
                        Workman workman = new Workman();
                        workman.setID(workmansRolesSqlRowSet.getInt("ID"));
                        workman.setName(workmansRolesSqlRowSet.getString("Name"));
                        workman.setPassword(workmansRolesSqlRowSet.getString("Password"));
                        workman.setFirstName(workmansRolesSqlRowSet.getString("FirstName"));
                        workman.setLastName(workmansRolesSqlRowSet.getString("LastName"));
                        workman.setPhones(workmansRolesSqlRowSet.getString("Phones"));
                        workman.setAddress(new Address());
                        workman.getAddress().setStreet(workmansRolesSqlRowSet.getString("Street"));
                        workman.getAddress().setPincode(workmansRolesSqlRowSet.getString("Pincode"));
                        workman.getAddress().setCity(workmansRolesSqlRowSet.getString("City"));
                        workman.getAddress().setState(workmansRolesSqlRowSet.getString("State"));
                        workman.getAddress().setCountry(workmansRolesSqlRowSet.getString("Country"));
                        role.getWorkmans().add(workman);
                    }
                }
                workmansRolesSqlRowSet.beforeFirst();
            }
        }
        return roleList;
    }
}