package net.bsoftlab.dao.template.jdbc.core;

import net.bsoftlab.dao.WorkmanDao;
import net.bsoftlab.dao.template.jdbc.mapper.PermissionMapper;
import net.bsoftlab.dao.template.jdbc.mapper.RoleMapper;
import net.bsoftlab.dao.template.jdbc.mapper.WorkmanMapper;

import net.bsoftlab.model.Permission;
import net.bsoftlab.model.Role;
import net.bsoftlab.model.Workman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.util.List;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WorkmanDaoJdbcTemplate implements WorkmanDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public WorkmanDaoJdbcTemplate(
            JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PreDestroy
    public void destroy(){}
    @PostConstruct
    public void init(){}

    @Override
    public void deleteWorkman(Workman workman) {
        String workmanRoleDeleteStatementSql =
                "DELETE FROM refworkmansroles " +
                        "WHERE refworkmansroles.WorkmanID = ? ";
        this.jdbcTemplate.update(workmanRoleDeleteStatementSql, workman.getID());

        String workmanDeleteStatementSql =
                "DELETE FROM refworkmans " +
                        "WHERE refworkmans.ID = ? ";
        this.jdbcTemplate.update(workmanDeleteStatementSql, workman.getID());
    }

    @Override
    public void insertWorkman(Workman workman) {
        String workmanInsertStatementSql =
                "INSERT INTO refworkmans " +
                        "(Name, Password, FirstName, LastName, Phones, " +
                        "Street, Pincode, City, State, Country) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        this.jdbcTemplate.update(workmanInsertStatementSql,
                workman.getName(),
                workman.getPassword(),
                workman.getFirstName(),
                workman.getLastName(),
                workman.getPhones(),
                workman.getAddress().getStreet(),
                workman.getAddress().getPincode(),
                workman.getAddress().getCity(),
                workman.getAddress().getState(),
                workman.getAddress().getCountry());

        String nameWorkmanSelectStatementSql =
                "SELECT refworkmans.ID " +
                        "FROM refworkmans " +
                        "WHERE refworkmans.Name = ?";
        Integer ID = this.jdbcTemplate
                .queryForObject(nameWorkmanSelectStatementSql,
                        new Object[]{workman.getName()}, Integer.class);
        workman.setID(ID);

        String workmanRoleInsertStatementSql =
                "INSERT INTO refworkmansroles " +
                        "(WorkmanID, RoleID) VALUES (?, ?)";
        for (Role role : workman.getRoles()) {
            this.jdbcTemplate
                    .update(workmanRoleInsertStatementSql,
                            workman.getID(), role.getID());
        }
    }

    @Override
    public void updateWorkman(Workman workman) {
        String workmanUpdateStatementSql =
                "UPDATE refworkmans " +
                        "SET refworkmans.Name = ?, " +
                        "refworkmans.Password = ?, " +
                        "refworkmans.FirstName = ?, " +
                        "refworkmans.LastName = ?, " +
                        "refworkmans.Phones = ?, " +
                        "refworkmans.Street = ?, " +
                        "refworkmans.Pincode = ?, " +
                        "refworkmans.City = ?, " +
                        "refworkmans.State = ?, " +
                        "refworkmans.Country = ? " +
                        "WHERE refworkmans.ID = ?";
        this.jdbcTemplate.update(workmanUpdateStatementSql,
                workman.getName(),
                workman.getPassword(),
                workman.getFirstName(),
                workman.getLastName(),
                workman.getPhones(),
                workman.getAddress().getStreet(),
                workman.getAddress().getPincode(),
                workman.getAddress().getCity(),
                workman.getAddress().getState(),
                workman.getAddress().getCountry(),
                workman.getID());

        String workmanRoleUpdateStatementSql =
                "UPDATE refworkmansroles " +
                        "SET refworkmansroles.WorkmanID = ?, " +
                        "refworkmansroles.RoleID = ? " +
                        "WHERE refworkmansroles.WorkmanID = ?";
        for (Role role : workman.getRoles()) {
            this.jdbcTemplate.update(workmanRoleUpdateStatementSql,
                    workman.getID(), role.getID(), workman.getID());
        }
    }

    @Override
    public Workman getWorkman(Integer ID) {
        try {
            String workmanSelectStatementSql =
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
                            "refworkmans.Country " +
                            "FROM refworkmans " +
                            "WHERE refworkmans.ID = ?";
            Workman workman = this.jdbcTemplate
                    .queryForObject(workmanSelectStatementSql,
                            new Object[]{ID},
                            new WorkmanMapper());

            String rolesWorkmanSelectStatementSql =
                    "SELECT refroles.ID, " +
                            "refroles.Name " +
                            "FROM refworkmansroles " +
                            "INNER JOIN refroles " +
                            "ON refworkmansroles.RoleID = refroles.ID " +
                            "WHERE refworkmansroles.WorkmanID = ?";
            List<Role> rolesWorkman = this.jdbcTemplate
                    .query(rolesWorkmanSelectStatementSql,
                            new Object[]{ID},
                            new RoleMapper());

            if (rolesWorkman == null || rolesWorkman.isEmpty()) {
                return workman;
            }

            workman.getRoles().addAll(rolesWorkman);

            for (Role role: workman.getRoles()) {
                String permissionsRoleSelectStatementSql =
                        "SELECT refpermissions.ID, " +
                                "refpermissions.Name " +
                                "FROM refrolespermissions " +
                                "INNER JOIN refpermissions " +
                                "ON refrolespermissions.PermissionID = refpermissions.ID " +
                                "WHERE refrolespermissions.RoleID = ? " +
                                "ORDER BY refrolespermissions.PermissionID ASC";
                List<Permission> permissionsRole = this.jdbcTemplate
                        .query(permissionsRoleSelectStatementSql,
                                new Object[]{role.getID()},
                                new PermissionMapper());

                if (permissionsRole != null && !permissionsRole.isEmpty()) {
                    role.getPermissions().addAll(permissionsRole);
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
                                "refworkmans.Country " +
                                "FROM refworkmansroles " +
                                "INNER JOIN refworkmans " +
                                "ON refworkmansroles.WorkmanID = refworkmans.ID " +
                                "WHERE refworkmansroles.RoleID = ? " +
                                "ORDER BY refworkmansroles.WorkmanID ASC";
                List<Workman> workmansRole = this.jdbcTemplate
                        .query(workmansRoleSelectStatementSql,
                                new Object[]{role.getID()},
                                new WorkmanMapper());

                if (workmansRole != null && !workmansRole.isEmpty()) {
                    role.getWorkmans().addAll(workmansRole);
                }
            }
            return workman;

        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return null;
        }
    }

    @Override
    public Workman getWorkman(Integer ID, String name) {
        try {
            String workmanSelectStatementSql =
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
                            "refworkmans.Country " +
                            "FROM refworkmans " +
                            "WHERE refworkmans.Name = ? " +
                            "AND NOT refworkmans.ID = ?";
            Workman workman = this.jdbcTemplate
                    .queryForObject(workmanSelectStatementSql,
                            new Object[]{name, ID},
                            new WorkmanMapper());

            String rolesWorkmanSelectStatementSql =
                    "SELECT refroles.ID, " +
                            "refroles.Name " +
                            "FROM refworkmansroles " +
                            "INNER JOIN refroles " +
                            "ON refworkmansroles.RoleID = refroles.ID " +
                            "WHERE refworkmansroles.WorkmanID = ? " +
                            "ORDER BY refworkmansroles.RoleID ASC";
            List<Role> rolesWorkman = this.jdbcTemplate
                    .query(rolesWorkmanSelectStatementSql,
                            new Object[]{workman.getID()},
                            new RoleMapper());

            if (rolesWorkman == null || rolesWorkman.isEmpty()) {
                return workman;
            }

            workman.getRoles().addAll(rolesWorkman);

            for (Role role : workman.getRoles()) {
                String permissionsRoleSelectStatementSql =
                        "SELECT refpermissions.ID, " +
                                "refpermissions.Name " +
                                "FROM refrolespermissions " +
                                "INNER JOIN refpermissions " +
                                "ON refrolespermissions.PermissionID = refpermissions.ID " +
                                "WHERE refrolespermissions.RoleID = ? " +
                                "ORDER BY refrolespermissions.PermissionID ASC";
                List<Permission> permissionsRole = this.jdbcTemplate
                        .query(permissionsRoleSelectStatementSql,
                                new Object[]{role.getID()},
                                new PermissionMapper());

                if (permissionsRole != null && !permissionsRole.isEmpty()) {
                    role.getPermissions().addAll(permissionsRole);
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
                                "refworkmans.Country " +
                                "FROM refworkmansroles " +
                                "INNER JOIN refworkmans " +
                                "ON refworkmansroles.WorkmanID = refworkmans.ID " +
                                "WHERE refworkmansroles.RoleID = ? " +
                                "ORDER BY refworkmansroles.WorkmanID ASC";
                List<Workman> workmansRole = this.jdbcTemplate
                        .query(workmansRoleSelectStatementSql,
                                new Object[]{role.getID()},
                                new WorkmanMapper());

                if (workmansRole != null && !workmansRole.isEmpty()) {
                    role.getWorkmans().addAll(workmansRole);
                }
            }
            return workman;

        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return null;
        }
    }

    @Override
    public List<Workman> getWorkmanList() {
        String workmansSelectStatementSql =
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
                        "refworkmans.Country " +
                        "FROM refworkmans " +
                        "ORDER BY refworkmans.ID ASC";
        List<Workman> workmanList = this.jdbcTemplate
                .query(workmansSelectStatementSql, new WorkmanMapper());

        if (workmanList == null || workmanList.isEmpty()) {
            return null;
        }

        String rolesSelectStatementSql =
                "SELECT refroles.ID, " +
                        "refroles.Name " +
                        "FROM refroles " +
                        "ORDER BY refroles.ID ASC";
        List<Role> roleList = this.jdbcTemplate
                .query(rolesSelectStatementSql, new RoleMapper());

        String permissionsSelectStatementSql =
                "SELECT refpermissions.ID, " +
                        "refpermissions.Name " +
                        "FROM refpermissions " +
                        "ORDER BY refpermissions.ID ASC";
        List<Permission> permissionList = this.jdbcTemplate
                .query(permissionsSelectStatementSql,
                        new PermissionMapper());

        String roleIDWorkmanIDSetSelectStatementSql =
                "SELECT refworkmansroles.WorkmanID, " +
                        "refworkmansroles.RoleID " +
                        "FROM refworkmansroles " +
                        "ORDER BY refworkmansroles.WorkmanID ASC, " +
                        "refworkmansroles.RoleID ASC";
        SqlRowSet roleIDWorkmanIDSqlRowSet = this.jdbcTemplate
                .queryForRowSet(roleIDWorkmanIDSetSelectStatementSql,
                        new EmptySqlParameterSource());

        if (roleIDWorkmanIDSqlRowSet == null) {
            return workmanList;
        }

        for (Workman workman : workmanList) {
            while (roleIDWorkmanIDSqlRowSet.next()) {
                if (workman.getID().equals(roleIDWorkmanIDSqlRowSet.getInt("WorkmanID"))) {
                    for (Role role : roleList) {
                        if (role.getID().equals(roleIDWorkmanIDSqlRowSet.getInt("RoleID"))) {
                            workman.getRoles().add(role);
                            break;
                        }
                    }
                }
            }
            roleIDWorkmanIDSqlRowSet.beforeFirst();
        }

        String permissionIDRoleIDSetSelectStatementSql =
                "SELECT refrolespermissions.RoleID, " +
                        "refrolespermissions.PermissionID " +
                        "FROM refrolespermissions " +
                        "ORDER BY refrolespermissions.RoleID ASC, " +
                        "refrolespermissions.PermissionID ASC";
        SqlRowSet permissionIDRoleIDSqlRowSet = this.jdbcTemplate
                .queryForRowSet(permissionIDRoleIDSetSelectStatementSql);

        if (permissionIDRoleIDSqlRowSet == null) {
            return workmanList;
        }

        for (Role role : roleList) {
            while (permissionIDRoleIDSqlRowSet.next()) {
                if (role.getID().equals(permissionIDRoleIDSqlRowSet.getInt("RoleID"))) {
                    for (Permission permission : permissionList) {
                        if (permission.getID().equals(permissionIDRoleIDSqlRowSet.getInt("PermissionID"))) {
                            role.getPermissions().add(permission);
                            break;
                        }
                    }
                }
            }
            permissionIDRoleIDSqlRowSet.beforeFirst();
        }
        return workmanList;
    }
}