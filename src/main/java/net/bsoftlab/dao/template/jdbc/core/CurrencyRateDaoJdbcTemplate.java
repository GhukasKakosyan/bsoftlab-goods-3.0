package net.bsoftlab.dao.template.jdbc.core;

import net.bsoftlab.dao.CurrencyRateDao;
import net.bsoftlab.dao.template.jdbc.mapper.CurrencyRateMapper;
import net.bsoftlab.model.Currency;
import net.bsoftlab.model.CurrencyRate;
import net.bsoftlab.utility.Functions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.util.Date;
import java.util.List;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CurrencyRateDaoJdbcTemplate implements CurrencyRateDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CurrencyRateDaoJdbcTemplate(
            JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PreDestroy
    public void destroy(){}
    @PostConstruct
    public void init(){}

    @Override
    public void deleteCurrencyRate(CurrencyRate currencyRate) {
        String currencyRateDeleteStatementSql =
                "DELETE FROM refcurrenciesrates " +
                        "WHERE refcurrenciesrates.ID = ? ";
        this.jdbcTemplate.update(currencyRateDeleteStatementSql, currencyRate.getID());
    }

    @Override
    public void insertCurrencyRate(CurrencyRate currencyRate) {
        String currencyRateInsertStatementSQL =
                "INSERT INTO refcurrenciesrates " +
                        "(CurrencyCode, Date, Rate, Quantity) " +
                        "VALUES (?, ?, ?, ?)";
        this.jdbcTemplate.update(currencyRateInsertStatementSQL,
                currencyRate.getCurrency().getCode(),
                currencyRate.getDate(),
                currencyRate.getRate(),
                currencyRate.getQuantity());
    }

    @Override
    public void updateCurrencyRate(CurrencyRate currencyRate) {
        String currencyRateUpdateStatementSQL =
                "UPDATE refcurrenciesrates " +
                        "SET refcurrenciesrates.CurrencyCode = ?, " +
                        "refcurrenciesrates.Date = ?, " +
                        "refcurrenciesrates.Rate = ?, " +
                        "refcurrenciesrates.Quantity = ? " +
                        "WHERE refcurrenciesrates.ID = ? ";
        this.jdbcTemplate.update(currencyRateUpdateStatementSQL,
                currencyRate.getCurrency().getCode(),
                currencyRate.getDate(),
                currencyRate.getRate(),
                currencyRate.getQuantity(),
                currencyRate.getID());
    }

    @Override
    public boolean existCurrencyRate(Currency currency) {
        try {
            String existCurrencyRateSelectStatementSql =
                    "SELECT refcurrenciesrates.CurrencyCode " +
                            "FROM refcurrenciesrates " +
                            "WHERE refcurrenciesrates.CurrencyCode = ? " +
                            "LIMIT 1";
            this.jdbcTemplate.queryForObject(existCurrencyRateSelectStatementSql,
                    new Object[]{currency.getCode()}, String.class);
            return true;
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return false;
        }
    }

    @Override
    public CurrencyRate getCurrencyRate(Integer ID) {
        try {
            String currencyRateSelectStatementSQL =
                    "SELECT refcurrenciesrates.ID, " +
                            "refcurrencies.Code AS CurCode, " +
                            "refcurrencies.ShortName AS CurShortName, " +
                            "refcurrencies.LongName AS CurLongName, " +
                            "refcurrencies.Country AS CurCountry, " +
                            "refcurrencies.AdditionalInformation AS CurAdditionalInformation, " +
                            "refcurrenciesrates.Date, " +
                            "refcurrenciesrates.Rate, " +
                            "refcurrenciesrates.Quantity " +
                            "FROM refcurrenciesrates " +
                            "INNER JOIN refcurrencies " +
                            "ON refcurrenciesrates.CurrencyCode = refcurrencies.Code " +
                            "WHERE refcurrenciesrates.ID = ? ";
            return this.jdbcTemplate.queryForObject(currencyRateSelectStatementSQL,
                    new Object[]{ID},
                    new CurrencyRateMapper());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return null;
        }
    }

    @Override
    public CurrencyRate getCurrencyRate(Currency currency, Date date, Integer ID) {
        try {
            String currencyRateSelectStatementSql =
                    "SELECT refcurrenciesrates.ID, " +
                            "refcurrencies.Code AS CurCode, " +
                            "refcurrencies.ShortName AS CurShortName, " +
                            "refcurrencies.LongName AS CurLongName, " +
                            "refcurrencies.Country AS CurCountry, " +
                            "refcurrencies.AdditionalInformation AS CurAdditionalInformation, " +
                            "refcurrenciesrates.Date, " +
                            "refcurrenciesrates.Rate, " +
                            "refcurrenciesrates.Quantity " +
                            "FROM refcurrenciesrates " +
                            "INNER JOIN refcurrencies " +
                            "ON refcurrenciesrates.CurrencyCode = refcurrencies.Code " +
                            "WHERE NOT refcurrenciesrates.ID = ? " +
                            "AND refcurrenciesrates.CurrencyCode = ? " +
                            "AND refcurrenciesrates.Date = ? ";
            return this.jdbcTemplate.queryForObject(currencyRateSelectStatementSql,
                    new Object[]{ID, currency.getCode(), date},
                    new CurrencyRateMapper());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return null;
        }
    }

    @Override
    public List<CurrencyRate> getCurrencyRateList(int start, int size) {
        String currencyRateListSelectStatementSql =
                "SELECT refcurrenciesrates.ID, " +
                        "refcurrencies.Code AS CurCode, " +
                        "refcurrencies.ShortName AS CurShortName, " +
                        "refcurrencies.LongName AS CurLongName, " +
                        "refcurrencies.Country AS CurCountry, " +
                        "refcurrencies.AdditionalInformation AS CurAdditionalInformation, " +
                        "refcurrenciesrates.Date, " +
                        "refcurrenciesrates.Rate, " +
                        "refcurrenciesrates.Quantity " +
                        "FROM refcurrenciesrates " +
                        "INNER JOIN refcurrencies " +
                        "ON refcurrenciesrates.CurrencyCode = refcurrencies.Code " +
                        "ORDER BY refcurrenciesrates.CurrencyCode ASC, " +
                        "refcurrenciesrates.Date DESC, " +
                        "refcurrenciesrates.ID DESC";
        List<CurrencyRate> currencyRateList = this.jdbcTemplate.query(
                currencyRateListSelectStatementSql, new CurrencyRateMapper());
        if (currencyRateList == null || currencyRateList.isEmpty()) {
            return null;
        }

        int first = Functions.calculateFirst(start, size, currencyRateList.size());
        int quantity = Functions.calculateQuantity(start, size, currencyRateList.size());
        List<CurrencyRate> currencyRateSubList = currencyRateList.subList(first, quantity);
        if(currencyRateSubList.isEmpty()) {
            return null;
        }
        return currencyRateSubList;
    }

    @Override
    public List<CurrencyRate> getCurrencyRateList(Currency currency, int start, int size) {
        String currencyRateListSelectStatementSql =
                "SELECT refcurrenciesrates.ID, " +
                        "refcurrencies.Code AS CurCode, " +
                        "refcurrencies.ShortName AS CurShortName, " +
                        "refcurrencies.LongName AS CurLongName, " +
                        "refcurrencies.Country AS CurCountry, " +
                        "refcurrencies.AdditionalInformation AS CurAdditionalInformation, " +
                        "refcurrenciesrates.Date, " +
                        "refcurrenciesrates.Rate, " +
                        "refcurrenciesrates.Quantity " +
                        "FROM refcurrenciesrates " +
                        "INNER JOIN refcurrencies " +
                        "ON refcurrenciesrates.CurrencyCode = refcurrencies.Code " +
                        "WHERE refcurrenciesrates.CurrencyCode = ? " +
                        "ORDER BY refcurrenciesrates.CurrencyCode ASC, " +
                        "refcurrenciesrates.Date DESC, " +
                        "refcurrenciesrates.ID DESC";
        List<CurrencyRate> currencyRateList = this.jdbcTemplate.query(
                currencyRateListSelectStatementSql,
                new Object[]{currency.getCode()}, new CurrencyRateMapper());
        if (currencyRateList == null || currencyRateList.isEmpty()) {
            return null;
        }

        int first = Functions.calculateFirst(start, size, currencyRateList.size());
        int quantity = Functions.calculateQuantity(start, size, currencyRateList.size());
        List<CurrencyRate> currencyRateSubList = currencyRateList.subList(first, quantity);
        if(currencyRateSubList.isEmpty()) {
            return null;
        }
        return currencyRateSubList;
    }
}