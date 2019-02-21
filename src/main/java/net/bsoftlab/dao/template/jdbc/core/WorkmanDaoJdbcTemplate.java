package net.bsoftlab.dao.template.jdbc.core;

import net.bsoftlab.dao.WorkmanDao;
import net.bsoftlab.dao.template.jdbc.mapper.RoleMapper;
import net.bsoftlab.dao.template.jdbc.mapper.WorkmanMapper;
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
                        "WHERE refworkmansroles.WorkmanID = ?";
        this.jdbcTemplate.update(workmanRoleDeleteStatementSql, workman.getID());

        String workmanDeleteStatementSql =
                "DELETE FROM refworkmans " +
                        "WHERE refworkmans.ID = ?";
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
        Integer ID = this.jdbcTemplate.queryForObject(nameWorkmanSelectStatementSql,
                new Object[]{workman.getName()}, Integer.class);
        workman.setID(ID);

        String workmanRoleInsertStatementSql =
                "INSERT INTO refworkmansroles " +
                        "(WorkmanID, RoleID) VALUES (?, ?)";
        for (Role role : workman.getRoles()) {
            this.jdbcTemplate.update(workmanRoleInsertStatementSql,
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
            Workman workman = this.jdbcTemplate.queryForObject(
                    workmanSelectStatementSql, new Object[]{ID}, new WorkmanMapper());

            String rolesWorkmanSelectStatementSql =
                    "SELECT refroles.ID, " +
                            "refroles.Name " +
                            "FROM refroles, refworkmansroles " +
                            "WHERE refworkmansroles.RoleID = refroles.ID " +
                            "AND refworkmansroles.WorkmanID = ?";
            List<Role> rolesWorkman = this.jdbcTemplate.query(
                    rolesWorkmanSelectStatementSql, new Object[]{ID}, new RoleMapper());
            if (rolesWorkman != null && !rolesWorkman.isEmpty()) {
                workman.getRoles().addAll(rolesWorkman);
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
            Workman workman = this.jdbcTemplate.queryForObject(
                    workmanSelectStatementSql, new Object[]{name, ID}, new WorkmanMapper());

            String rolesWorkmanSelectStatementSql =
                    "SELECT refroles.ID, " +
                            "refroles.Name " +
                            "FROM refroles, refworkmansroles " +
                            "WHERE refroles.ID = refworkmansroles.RoleID " +
                            "AND refworkmansroles.WorkmanID = ?";
            List<Role> rolesWorkman = this.jdbcTemplate.query(
                    rolesWorkmanSelectStatementSql,
                    new Object[]{workman.getID()}, new RoleMapper());
            if (rolesWorkman != null && !rolesWorkman.isEmpty()) {
                workman.getRoles().addAll(rolesWorkman);
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
        List<Workman> workmanList = this.jdbcTemplate.query(
                workmansSelectStatementSql, new WorkmanMapper());
        if (workmanList == null || workmanList.isEmpty()) {
            return null;
        }

        String rolesWorkmansSelectStatementSql =
                "SELECT refworkmansroles.WorkmanID, " +
                        "refroles.ID AS RoleID, " +
                        "refroles.Name AS RoleName " +
                        "FROM refroles, refworkmansroles " +
                        "WHERE refroles.ID = refworkmansroles.RoleID " +
                        "ORDER BY refworkmansroles.WorkmanID ASC, " +
                        "refworkmansroles.RoleID ASC";
        SqlRowSet rolesWorkmansSqlRowSet = this.jdbcTemplate.queryForRowSet(
                rolesWorkmansSelectStatementSql, new EmptySqlParameterSource());

        if (rolesWorkmansSqlRowSet != null) {
            for (Workman workman : workmanList) {
                while (rolesWorkmansSqlRowSet.next()) {
                    if (workman.getID().equals(rolesWorkmansSqlRowSet.getInt("WorkmanID"))) {
                        Role role = new Role();
                        role.setID(rolesWorkmansSqlRowSet.getInt("RoleID"));
                        role.setName(rolesWorkmansSqlRowSet.getString("RoleName"));
                        workman.getRoles().add(role);
                    }
                }
                rolesWorkmansSqlRowSet.beforeFirst();
            }
        }
        return workmanList;
    }
}