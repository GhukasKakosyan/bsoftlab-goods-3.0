package net.bsoftlab.dao.template.jdbc.core;

import net.bsoftlab.dao.UnitofmsrDao;
import net.bsoftlab.dao.template.jdbc.mapper.UnitofmsrMapper;
import net.bsoftlab.model.Unitofmsr;
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
public class UnitofmsrDaoJdbcTemplate implements UnitofmsrDao {

    private JdbcTemplate jdbcTemplate;
    private UtilityFunctions utilityFunctions = null;

    @Autowired
    public UnitofmsrDaoJdbcTemplate(
            JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PreDestroy
    public void destroy(){}
    @PostConstruct
    public void init(){}

    @Autowired
    public void setUtilityFunctions(
            UtilityFunctions utilityFunctions) {
        this.utilityFunctions = utilityFunctions;
    }

    @Override
    public void deleteUnitofmsr(Unitofmsr unitofmsr) {
        String unitofmsrDeleteStatementSql =
                "DELETE FROM refunitsofmsrs " +
                        "WHERE refunitsofmsrs.Code = ?";
        this.jdbcTemplate.update(unitofmsrDeleteStatementSql, unitofmsr.getCode());
    }

    @Override
    public void insertUnitofmsr(Unitofmsr unitofmsr) {
        String unitofmsrInsertStatementSql =
                "INSERT INTO refunitsofmsrs " +
                        "(Code, ShortName, LongName) " +
                        "VALUES (?, ?, ?)";
        this.jdbcTemplate.update(unitofmsrInsertStatementSql,
                unitofmsr.getCode(),
                unitofmsr.getShortName(),
                unitofmsr.getLongName());
    }

    @Override
    public void updateUnitofmsr(Unitofmsr unitofmsr) {
        String unitofmsrUpdateStatementSql =
                "UPDATE refunitsofmsrs " +
                        "SET refunitsofmsrs.ShortName = ?, " +
                        "refunitsofmsrs.LongName = ? " +
                        "WHERE refunitsofmsrs.Code = ?";
        this.jdbcTemplate.update(unitofmsrUpdateStatementSql,
                unitofmsr.getShortName(),
                unitofmsr.getLongName(),
                unitofmsr.getCode());
    }

    @Override
    public Unitofmsr getUnitofmsr(String code) {
        try {
            String unitofmsrSelectStatementSql =
                    "SELECT refunitsofmsrs.Code, " +
                            "refunitsofmsrs.ShortName, " +
                            "refunitsofmsrs.LongName " +
                            "FROM refunitsofmsrs " +
                            "WHERE refunitsofmsrs.Code = ?";
            return this.jdbcTemplate
                    .queryForObject(unitofmsrSelectStatementSql,
                            new Object[]{code}, new UnitofmsrMapper());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return null;
        }
    }

    @Override
    public List<Unitofmsr> getUnitofmsrList(int start, int size) {

        String unitofmsrListSelectStatementSql =
                "SELECT refunitsofmsrs.Code, " +
                        "refunitsofmsrs.ShortName, " +
                        "refunitsofmsrs.LongName " +
                        "FROM refunitsofmsrs " +
                        "ORDER BY refunitsofmsrs.Code";
        List<Unitofmsr> unitofmsrList = this.jdbcTemplate
                .query(unitofmsrListSelectStatementSql, new UnitofmsrMapper());
        if (unitofmsrList == null || unitofmsrList.isEmpty()) {
            return null;
        }

        int first = this.utilityFunctions.calculateFirst(start, size, unitofmsrList.size());
        int quantity = this.utilityFunctions.calculateQuantity(start, size, unitofmsrList.size());
        List<Unitofmsr> unitofmsrSubList = unitofmsrList.subList(first, quantity);
        if(unitofmsrSubList.isEmpty()) {
            return null;
        }
        return unitofmsrSubList;
    }
}