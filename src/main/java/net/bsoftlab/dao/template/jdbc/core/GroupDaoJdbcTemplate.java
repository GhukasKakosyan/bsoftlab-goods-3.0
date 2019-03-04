package net.bsoftlab.dao.template.jdbc.core;

import net.bsoftlab.dao.GroupDao;
import net.bsoftlab.dao.template.jdbc.mapper.GroupMapper;
import net.bsoftlab.model.Group;
import net.bsoftlab.utility.Functions;

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
public class GroupDaoJdbcTemplate implements GroupDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GroupDaoJdbcTemplate(
            JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @PreDestroy
    public void destroy() {}
    @PostConstruct
    public void init() {}

    @Override
    public void deleteGroup(Group group) {
        String groupDeleteStatementSql =
                "DELETE FROM refgroups " +
                        "WHERE refgroups.Code = ?";
        this.jdbcTemplate.update(groupDeleteStatementSql, group.getCode());
    }

    @Override
    public void insertGroup(Group group) {
        String groupInsertStatementSql =
                "INSERT INTO refgroups " +
                        "(Code, Name, EnhancedName) " +
                        "VALUES (?, ?, ?)";
        this.jdbcTemplate.update(groupInsertStatementSql,
                group.getCode(),
                group.getName(),
                group.getEnhancedName());
    }

    @Override
    public void updateGroup(Group group) {
        String groupUpdateStatementSql =
                "UPDATE refgroups " +
                        "SET refgroups.Name = ?, " +
                        "refgroups.EnhancedName = ?" +
                        "WHERE refgroups.Code = ?";
        this.jdbcTemplate.update(groupUpdateStatementSql,
                group.getName(),
                group.getEnhancedName(),
                group.getCode());
    }

    @Override
    public Group getGroup(String code) {
        try {
            String groupSelectStatementSql =
                    "SELECT refgroups.Code, " +
                            "refgroups.Name, " +
                            "refgroups.EnhancedName " +
                            "FROM refgroups " +
                            "WHERE refgroups.Code = ?";
            return this.jdbcTemplate.queryForObject(groupSelectStatementSql,
                    new Object[]{code}, new GroupMapper());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return null;
        }
    }

    @Override
    public List<Group> getGroupList(int start, int size) {
        List<Group> groupList;
        String groupListSelectStatementSql =
                "SELECT refgroups.Code, " +
                        "refgroups.Name, " +
                        "refgroups.EnhancedName " +
                        "FROM refgroups " +
                        "ORDER BY refgroups.Code";
        groupList = this.jdbcTemplate
                .query(groupListSelectStatementSql, new GroupMapper());
        if (groupList == null || groupList.isEmpty()) {
            return null;
        }

        int first = Functions.calculateFirst(start, size, groupList.size());
        int quantity = Functions.calculateQuantity(start, size, groupList.size());
        List<Group> groupSubList = groupList.subList(first, quantity);
        if(groupSubList.isEmpty()) {
            return null;
        }
        return groupSubList;
    }
}