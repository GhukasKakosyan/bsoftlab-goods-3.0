package net.bsoftlab.dao.template.jdbc.core;

import net.bsoftlab.dao.api.CurrencyDao;
import net.bsoftlab.dao.template.jdbc.mapper.CurrencyMapper;
import net.bsoftlab.model.Currency;
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
public class CurrencyDaoJdbcTemplate implements CurrencyDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CurrencyDaoJdbcTemplate(
            JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @PreDestroy
    public void destroy() {}
    @PostConstruct
    public void init() {}

    @Override
    public void deleteCurrency(Currency currency) {
        String currencyDeleteStatementSql =
                "DELETE FROM refcurrencies " +
                        "WHERE refcurrencies.Code = ?";
        this.jdbcTemplate.update(currencyDeleteStatementSql, currency.getCode());
    }

    @Override
    public void insertCurrency(Currency currency) {
        String currencyInsertStatementSQL =
                "INSERT INTO refcurrencies " +
                        "(Code, ShortName, LongName, Country, AdditionalInformation) " +
                        "VALUES (?, ?, ?, ?, ?)";
        this.jdbcTemplate.update(currencyInsertStatementSQL,
                currency.getCode(),
                currency.getShortName(),
                currency.getLongName(),
                currency.getCountry(),
                currency.getAdditionalInformation());
    }

    @Override
    public void updateCurrency(Currency currency) {
        String currencyUpdateStatementSQL =
                "UPDATE refcurrencies " +
                        "SET refcurrencies.ShortName = ?, " +
                        "refcurrencies.LongName = ?, " +
                        "refcurrencies.Country = ?, " +
                        "refcurrencies.AdditionalInformation = ? " +
                        "WHERE refcurrencies.Code = ?";
        this.jdbcTemplate.update(currencyUpdateStatementSQL,
                currency.getShortName(),
                currency.getLongName(),
                currency.getCountry(),
                currency.getAdditionalInformation(),
                currency.getCode());
    }

    @Override
    public Currency getCurrency(String code) {
        try {
            String currencySelectStatementSQL =
                    "SELECT refcurrencies.Code, " +
                            "refcurrencies.ShortName, " +
                            "refcurrencies.LongName," +
                            "refcurrencies.Country, " +
                            "refcurrencies.AdditionalInformation " +
                            "FROM refcurrencies " +
                            "WHERE refcurrencies.Code = ?";
            return this.jdbcTemplate.queryForObject(
                    currencySelectStatementSQL,
                    new Object[]{code}, new CurrencyMapper());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return null;
        }
    }

    @Override
    public List<Currency> getCurrencyList(int start, int size) {
        String currencyListSelectStatementSql =
                "SELECT refcurrencies.Code, " +
                        "refcurrencies.ShortName, " +
                        "refcurrencies.LongName," +
                        "refcurrencies.Country, " +
                        "refcurrencies.AdditionalInformation " +
                        "FROM refcurrencies " +
                        "ORDER BY refcurrencies.Code ASC";
        List<Currency> currencyList = this.jdbcTemplate
                .query(currencyListSelectStatementSql, new CurrencyMapper());
        if (currencyList == null || currencyList.isEmpty()) {
            return null;
        }

        int first = Functions.calculateFirst(start, size, currencyList.size());
        int quantity = Functions.calculateQuantity(start, size, currencyList.size());
        List<Currency> currencySubList = currencyList.subList(first, quantity);
        if(currencySubList.isEmpty()) {
            return null;
        }
        return currencySubList;
    }
}