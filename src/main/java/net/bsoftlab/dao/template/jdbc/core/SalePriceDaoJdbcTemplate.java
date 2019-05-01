package net.bsoftlab.dao.template.jdbc.core;

import net.bsoftlab.dao.api.SalePriceDao;
import net.bsoftlab.dao.template.jdbc.mapper.SalePriceMapper;
import net.bsoftlab.model.Currency;
import net.bsoftlab.model.Department;
import net.bsoftlab.model.Matvalue;
import net.bsoftlab.model.SalePrice;
import net.bsoftlab.utility.Functions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SalePriceDaoJdbcTemplate implements SalePriceDao {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public SalePriceDaoJdbcTemplate(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @PreDestroy
    public void destroy(){}
    @PostConstruct
    public void init(){}

    @Override
    public void deleteSalePrice(SalePrice salePrice) {
        String salePriceDeleteStatementSql =
                "DELETE FROM refpricesofmatvalues " +
                        "WHERE refpricesofmatvalues.ID = :idSalePriceParameter";
        Map<String, Integer> mapParameterSalePriceDeleteStatementSql =
                Collections.singletonMap("idSalePriceParameter", salePrice.getID());
        this.namedParameterJdbcTemplate
                .update(salePriceDeleteStatementSql,
                        mapParameterSalePriceDeleteStatementSql);
    }

    @Override
    public void insertSalePrice(SalePrice salePrice) {
        String salePriceInsertStatementSql =
                "INSERT INTO refpricesofmatvalues " +
                        "(MatvalueCode, DepartmentCode, CurrencyCode, Date, Price, Quantity) " +
                        "VALUES (:codeMatvalueParameter, " +
                        ":codeDepartmentParameter, " +
                        ":codeCurrencyParameter, " +
                        ":dateParameter, " +
                        ":priceParameter, " +
                        ":quantityParameter)";
        SqlParameterSource sqlParameterSourceSalePriceInsertStatementSql =
                new MapSqlParameterSource()
                        .addValue("codeMatvalueParameter", salePrice.getMatvalue().getCode())
                        .addValue("codeDepartmentParameter", salePrice.getDepartment().getCode())
                        .addValue("codeCurrencyParameter", salePrice.getCurrency().getCode())
                        .addValue("dateParameter", salePrice.getDate())
                        .addValue("priceParameter", salePrice.getPrice())
                        .addValue("quantityParameter", salePrice.getQuantity());
        this.namedParameterJdbcTemplate
                .update(salePriceInsertStatementSql,
                        sqlParameterSourceSalePriceInsertStatementSql);
    }

    @Override
    public void updateSalePrice(SalePrice salePrice) {
        String salePriceUpdateStatementSql =
                "UPDATE refpricesofmatvalues " +
                        "SET refpricesofmatvalues.MatvalueCode = :codeMatvalueParameter, " +
                        "refpricesofmatvalues.DepartmentCode = :codeDepartmentParameter, " +
                        "refpricesofmatvalues.CurrencyCode = :codeCurrencyParameter, " +
                        "refpricesofmatvalues.Date = :dateParameter, " +
                        "refpricesofmatvalues.Price = :priceParameter, " +
                        "refpricesofmatvalues.Quantity = :quantityParameter " +
                        "WHERE refpricesofmatvalues.ID = :idSalePriceParameter";
        SqlParameterSource sqlParameterSourceSalePriceUpdateStatementSql =
                new MapSqlParameterSource()
                        .addValue("codeMatvalueParameter", salePrice.getMatvalue().getCode())
                        .addValue("codeDepartmentParameter", salePrice.getDepartment().getCode())
                        .addValue("codeCurrencyParameter", salePrice.getCurrency().getCode())
                        .addValue("dateParameter", salePrice.getDate())
                        .addValue("priceParameter", salePrice.getPrice())
                        .addValue("quantityParameter", salePrice.getQuantity())
                        .addValue("idSalePriceParameter", salePrice.getID());
        this.namedParameterJdbcTemplate
                .update(salePriceUpdateStatementSql,
                        sqlParameterSourceSalePriceUpdateStatementSql);
    }

    @Override
    public boolean existSalePrice(Currency currency) {
        try {
            String existSalePriceCurrencySelectStatementSql =
                    "SELECT refpricesofmatvalues.CurrencyCode " +
                            "FROM refpricesofmatvalues " +
                            "WHERE CurrencyCode = :curCodeParameter LIMIT 1";
            this.namedParameterJdbcTemplate.queryForObject(
                    existSalePriceCurrencySelectStatementSql,
                    new MapSqlParameterSource("curCodeParameter", currency.getCode()),
                    String.class);
            return true;
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return false;
        }
    }

    @Override
    public boolean existSalePrice(Department department) {
        try {
            String existSalePriceDepartmentSelectStatementSql =
                    "SELECT refpricesofmatvalues.DepartmentCode " +
                            "FROM refpricesofmatvalues " +
                            "WHERE DepartmentCode = :depCodeParameter LIMIT 1";
            this.namedParameterJdbcTemplate.queryForObject(
                    existSalePriceDepartmentSelectStatementSql,
                    new MapSqlParameterSource("depCodeParameter", department.getCode()),
                    String.class);
            return true;
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return false;
        }
    }

    @Override
    public SalePrice getSalePrice(Integer ID) {
        try {
            String salePriceSelectStatementSql =
                    "SELECT refpricesofmatvalues.ID, " +
                            "refmatvalues.Code AS MatCode, " +
                            "refmatvalues.Name AS MatName, " +
                            "refunitsofmsrs.Code AS MsrCode, " +
                            "refunitsofmsrs.ShortName AS MsrShortName, " +
                            "refunitsofmsrs.LongName AS MsrLongName, " +
                            "refgroups.Code AS GrpCode, " +
                            "refgroups.Name AS GrpName, " +
                            "refgroups.EnhancedName AS GrpEnhancedName, " +
                            "refdepartments.Code AS DptCode, " +
                            "refdepartments.Name AS DptName, " +
                            "refdepartments.Street AS DptStreet, " +
                            "refdepartments.Pincode AS DptPincode, " +
                            "refdepartments.City AS DptCity, " +
                            "refdepartments.State AS DptState, " +
                            "refdepartments.Country AS DptCountry, " +
                            "refdepartments.Phones AS DptPhones, " +
                            "refdepartments.Faxes AS DptFaxes, " +
                            "refdepartments.WebSite AS DptWebSite, " +
                            "refdepartments.EmailAddress AS DptEmailAddress, " +
                            "refdepartments.AdditionalInformation AS DptAdditionalInformation, " +
                            "refcurrencies.Code AS CurCode, " +
                            "refcurrencies.ShortName AS CurShortName, " +
                            "refcurrencies.LongName AS CurLongName, " +
                            "refcurrencies.Country AS CurCountry, " +
                            "refcurrencies.AdditionalInformation AS CurAdditionalInformation, " +
                            "refpricesofmatvalues.Date, " +
                            "refpricesofmatvalues.Price, " +
                            "refpricesofmatvalues.Quantity " +
                            "FROM refpricesofmatvalues " +
                            "INNER JOIN refcurrencies " +
                            "ON refpricesofmatvalues.CurrencyCode = refcurrencies.Code " +
                            "INNER JOIN refdepartments " +
                            "ON refpricesofmatvalues.DepartmentCode = refdepartments.Code " +
                            "INNER JOIN refmatvalues " +
                            "ON refpricesofmatvalues.MatvalueCode = refmatvalues.Code " +
                            "INNER JOIN refgroups " +
                            "ON refmatvalues.GroupCode = refgroups.Code " +
                            "INNER JOIN refunitsofmsrs " +
                            "ON refmatvalues.UnitofmsrCode = refunitsofmsrs.Code " +
                            "WHERE refpricesofmatvalues.ID = :idSalePriceParameter";
            return this.namedParameterJdbcTemplate
                    .queryForObject(salePriceSelectStatementSql,
                            new MapSqlParameterSource("idSalePriceParameter", ID),
                            new SalePriceMapper());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return null;
        }
    }

    @Override
    public SalePrice getSalePrice(
            Department department, Matvalue matvalue, Date date, Integer ID) {
        try {
            String salePriceSelectStatementSql =
                    "SELECT refpricesofmatvalues.ID, " +
                            "refmatvalues.Code AS MatCode, " +
                            "refmatvalues.Name AS MatName, " +
                            "refunitsofmsrs.Code AS MsrCode, " +
                            "refunitsofmsrs.ShortName AS MsrShortName, " +
                            "refunitsofmsrs.LongName AS MsrLongName, " +
                            "refgroups.Code AS GrpCode, " +
                            "refgroups.Name AS GrpName, " +
                            "refgroups.EnhancedName AS GrpEnhancedName, " +
                            "refdepartments.Code AS DptCode, " +
                            "refdepartments.Name AS DptName, " +
                            "refdepartments.Street AS DptStreet, " +
                            "refdepartments.Pincode AS DptPincode, " +
                            "refdepartments.City AS DptCity, " +
                            "refdepartments.State AS DptState, " +
                            "refdepartments.Country AS DptCountry, " +
                            "refdepartments.Phones AS DptPhones, " +
                            "refdepartments.Faxes AS DptFaxes, " +
                            "refdepartments.WebSite AS DptWebSite, " +
                            "refdepartments.EmailAddress AS DptEmailAddress, " +
                            "refdepartments.AdditionalInformation AS DptAdditionalInformation, " +
                            "refcurrencies.Code AS CurCode, " +
                            "refcurrencies.ShortName AS CurShortName, " +
                            "refcurrencies.LongName AS CurLongName, " +
                            "refcurrencies.Country AS CurCountry, " +
                            "refcurrencies.AdditionalInformation AS CurAdditionalInformation, " +
                            "refpricesofmatvalues.Date, " +
                            "refpricesofmatvalues.Price, " +
                            "refpricesofmatvalues.Quantity " +
                            "FROM refpricesofmatvalues " +
                            "INNER JOIN refcurrencies " +
                            "ON refpricesofmatvalues.CurrencyCode = refcurrencies.Code " +
                            "INNER JOIN refdepartments " +
                            "ON refpricesofmatvalues.DepartmentCode = refdepartments.Code " +
                            "INNER JOIN refmatvalues " +
                            "ON refpricesofmatvalues.MatvalueCode = refmatvalues.Code " +
                            "INNER JOIN refgroups " +
                            "ON refmatvalues.GroupCode = refgroups.Code " +
                            "INNER JOIN refunitsofmsrs " +
                            "ON refmatvalues.UnitofmsrCode = refunitsofmsrs.Code " +
                            "WHERE refpricesofmatvalues.DepartmentCode = :departmentCodeParameter " +
                            "AND refpricesofmatvalues.MatvalueCode = :matvalueCodeParameter " +
                            "AND refpricesofmatvalues.Date = :dateParameter " +
                            "AND NOT refpricesofmatvalues.ID = :IDParameter";
            SqlParameterSource sqlParameterSourceSalePriceSelectStatementSql =
                    new MapSqlParameterSource()
                            .addValue("departmentCodeParameter", department.getCode())
                            .addValue("matvalueCodeParameter", matvalue.getCode())
                            .addValue("dateParameter", date)
                            .addValue("IDParameter", ID);
            return this.namedParameterJdbcTemplate
                    .queryForObject(salePriceSelectStatementSql,
                            sqlParameterSourceSalePriceSelectStatementSql,
                            new SalePriceMapper());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return null;
        }
    }

    @Override
    public List<SalePrice> getSalePriceList(int start, int size) {
        String salePriceListSelectStatementSql =
                "SELECT refpricesofmatvalues.ID, " +
                        "refmatvalues.Code AS MatCode, " +
                        "refmatvalues.Name AS MatName, " +
                        "refunitsofmsrs.Code AS MsrCode, " +
                        "refunitsofmsrs.ShortName AS MsrShortName, " +
                        "refunitsofmsrs.LongName AS MsrLongName, " +
                        "refgroups.Code AS GrpCode, " +
                        "refgroups.Name AS GrpName, " +
                        "refgroups.EnhancedName AS GrpEnhancedName, " +
                        "refdepartments.Code AS DptCode, " +
                        "refdepartments.Name AS DptName, " +
                        "refdepartments.Street AS DptStreet, " +
                        "refdepartments.Pincode AS DptPincode, " +
                        "refdepartments.City AS DptCity, " +
                        "refdepartments.State AS DptState, " +
                        "refdepartments.Country AS DptCountry, " +
                        "refdepartments.Phones AS DptPhones, " +
                        "refdepartments.Faxes AS DptFaxes, " +
                        "refdepartments.WebSite AS DptWebSite, " +
                        "refdepartments.EmailAddress AS DptEmailAddress, " +
                        "refdepartments.AdditionalInformation AS DptAdditionalInformation, " +
                        "refcurrencies.Code AS CurCode, " +
                        "refcurrencies.ShortName AS CurShortName, " +
                        "refcurrencies.LongName AS CurLongName, " +
                        "refcurrencies.Country AS CurCountry, " +
                        "refcurrencies.AdditionalInformation AS CurAdditionalInformation, " +
                        "refpricesofmatvalues.Date, " +
                        "refpricesofmatvalues.Price, " +
                        "refpricesofmatvalues.Quantity " +
                        "FROM refpricesofmatvalues " +
                        "INNER JOIN refcurrencies " +
                        "ON refpricesofmatvalues.CurrencyCode = refcurrencies.Code " +
                        "INNER JOIN refdepartments " +
                        "ON refpricesofmatvalues.DepartmentCode = refdepartments.Code " +
                        "INNER JOIN refmatvalues " +
                        "ON refpricesofmatvalues.MatvalueCode = refmatvalues.Code " +
                        "INNER JOIN refgroups " +
                        "ON refmatvalues.GroupCode = refgroups.Code " +
                        "INNER JOIN refunitsofmsrs " +
                        "ON refmatvalues.UnitofmsrCode = refunitsofmsrs.Code " +
                        "ORDER BY refpricesofmatvalues.DepartmentCode ASC, " +
                        "refpricesofmatvalues.MatvalueCode ASC, " +
                        "refpricesofmatvalues.Date DESC, " +
                        "refpricesofmatvalues.ID DESC";
        List<SalePrice> salePriceList = this.namedParameterJdbcTemplate
                .query(salePriceListSelectStatementSql,
                        new EmptySqlParameterSource(),
                        new SalePriceMapper());
        if (salePriceList == null || salePriceList.isEmpty()) {
            return null;
        }

        int first = Functions.calculateFirst(start, size, salePriceList.size());
        int quantity = Functions.calculateQuantity(start, size, salePriceList.size());
        List<SalePrice> salePriceSubList = salePriceList.subList(first, quantity);
        if(salePriceSubList.isEmpty()) {
            return null;
        }
        return salePriceSubList;
    }

    @Override
    public List<SalePrice> getSalePriceListCurrency(
            Currency currency, int start, int size) {

        String salePriceListCurrencySelectStatementSql =
                "SELECT refpricesofmatvalues.ID, " +
                        "refmatvalues.Code AS MatCode, " +
                        "refmatvalues.Name AS MatName, " +
                        "refunitsofmsrs.Code AS MsrCode, " +
                        "refunitsofmsrs.ShortName AS MsrShortName, " +
                        "refunitsofmsrs.LongName AS MsrLongName, " +
                        "refgroups.Code AS GrpCode, " +
                        "refgroups.Name AS GrpName, " +
                        "refgroups.EnhancedName AS GrpEnhancedName, " +
                        "refdepartments.Code AS DptCode, " +
                        "refdepartments.Name AS DptName, " +
                        "refdepartments.Street AS DptStreet, " +
                        "refdepartments.Pincode AS DptPincode, " +
                        "refdepartments.City AS DptCity, " +
                        "refdepartments.State AS DptState, " +
                        "refdepartments.Country AS DptCountry, " +
                        "refdepartments.Phones AS DptPhones, " +
                        "refdepartments.Faxes AS DptFaxes, " +
                        "refdepartments.WebSite AS DptWebSite, " +
                        "refdepartments.EmailAddress AS DptEmailAddress, " +
                        "refdepartments.AdditionalInformation AS DptAdditionalInformation, " +
                        "refcurrencies.Code AS CurCode, " +
                        "refcurrencies.ShortName AS CurShortName, " +
                        "refcurrencies.LongName AS CurLongName, " +
                        "refcurrencies.Country AS CurCountry, " +
                        "refcurrencies.AdditionalInformation AS CurAdditionalInformation, " +
                        "refpricesofmatvalues.Date, " +
                        "refpricesofmatvalues.Price, " +
                        "refpricesofmatvalues.Quantity " +
                        "FROM refpricesofmatvalues " +
                        "INNER JOIN refcurrencies " +
                        "ON refpricesofmatvalues.CurrencyCode = refcurrencies.Code " +
                        "INNER JOIN refdepartments " +
                        "ON refpricesofmatvalues.DepartmentCode = refdepartments.Code " +
                        "INNER JOIN refmatvalues " +
                        "ON refpricesofmatvalues.MatvalueCode = refmatvalues.Code " +
                        "INNER JOIN refunitsofmsrs " +
                        "ON refmatvalues.UnitofmsrCode = refunitsofmsrs.Code " +
                        "INNER JOIN refgroups " +
                        "ON refmatvalues.GroupCode = refgroups.Code " +
                        "WHERE refpricesofmatvalues.CurrencyCode = :currencyCodeParameter " +
                        "ORDER BY refpricesofmatvalues.DepartmentCode ASC, " +
                        "refpricesofmatvalues.MatvalueCode ASC, " +
                        "refpricesofmatvalues.Date DESC, " +
                        "refpricesofmatvalues.ID DESC ";
        List<SalePrice> salePriceListCurrency = this.namedParameterJdbcTemplate
                .query(salePriceListCurrencySelectStatementSql,
                        new MapSqlParameterSource("currencyCodeParameter", currency.getCode()),
                        new SalePriceMapper());
        if (salePriceListCurrency == null || salePriceListCurrency.isEmpty()) {
            return null;
        }

        int first = Functions.calculateFirst(start, size, salePriceListCurrency.size());
        int quantity = Functions.calculateQuantity(start, size, salePriceListCurrency.size());
        List<SalePrice> salePriceSubListCurrency =
                salePriceListCurrency.subList(first, quantity);
        if(salePriceSubListCurrency.isEmpty()) {
            return null;
        }
        return salePriceSubListCurrency;
    }

    @Override
    public List<SalePrice> getSalePriceListDepartment(
            Department department, int start, int size) {

        String salePriceListDepartmentSelectStatementSql =
                "SELECT refpricesofmatvalues.ID, " +
                        "refmatvalues.Code AS MatCode, " +
                        "refmatvalues.Name AS MatName, " +
                        "refunitsofmsrs.Code AS MsrCode, " +
                        "refunitsofmsrs.ShortName AS MsrShortName, " +
                        "refunitsofmsrs.LongName AS MsrLongName, " +
                        "refgroups.Code AS GrpCode, " +
                        "refgroups.Name AS GrpName, " +
                        "refgroups.EnhancedName AS GrpEnhancedName, " +
                        "refdepartments.Code AS DptCode, " +
                        "refdepartments.Name AS DptName, " +
                        "refdepartments.Street AS DptStreet, " +
                        "refdepartments.Pincode AS DptPincode, " +
                        "refdepartments.City AS DptCity, " +
                        "refdepartments.State AS DptState, " +
                        "refdepartments.Country AS DptCountry, " +
                        "refdepartments.Phones AS DptPhones, " +
                        "refdepartments.Faxes AS DptFaxes, " +
                        "refdepartments.WebSite AS DptWebSite, " +
                        "refdepartments.EmailAddress AS DptEmailAddress, " +
                        "refdepartments.AdditionalInformation AS DptAdditionalInformation, " +
                        "refcurrencies.Code AS CurCode, " +
                        "refcurrencies.ShortName AS CurShortName, " +
                        "refcurrencies.LongName AS CurLongName, " +
                        "refcurrencies.Country AS CurCountry, " +
                        "refcurrencies.AdditionalInformation AS CurAdditionalInformation, " +
                        "refpricesofmatvalues.Date, " +
                        "refpricesofmatvalues.Price, " +
                        "refpricesofmatvalues.Quantity " +
                        "FROM refpricesofmatvalues " +
                        "INNER JOIN refcurrencies " +
                        "ON refpricesofmatvalues.CurrencyCode = refcurrencies.Code " +
                        "INNER JOIN refdepartments " +
                        "ON refpricesofmatvalues.DepartmentCode = refdepartments.Code " +
                        "INNER JOIN refmatvalues " +
                        "ON refpricesofmatvalues.MatvalueCode = refmatvalues.Code " +
                        "INNER JOIN refunitsofmsrs " +
                        "ON refmatvalues.UnitofmsrCode = refunitsofmsrs.Code " +
                        "INNER JOIN refgroups " +
                        "ON refmatvalues.GroupCode = refgroups.Code " +
                        "WHERE refpricesofmatvalues.DepartmentCode = :departmentCodeParameter " +
                        "ORDER BY refpricesofmatvalues.DepartmentCode ASC, " +
                        "refpricesofmatvalues.MatvalueCode ASC, " +
                        "refpricesofmatvalues.Date DESC, " +
                        "refpricesofmatvalues.ID DESC ";
        List<SalePrice> salePriceListDepartment = this.namedParameterJdbcTemplate
                .query(salePriceListDepartmentSelectStatementSql,
                        new MapSqlParameterSource("departmentCodeParameter", department.getCode()),
                        new SalePriceMapper());
        if (salePriceListDepartment == null || salePriceListDepartment.isEmpty()) {
            return null;
        }

        int first = Functions.calculateFirst(start, size, salePriceListDepartment.size());
        int quantity = Functions.calculateQuantity(start, size, salePriceListDepartment.size());
        List<SalePrice> salePriceSubListDepartment =
                salePriceListDepartment.subList(first, quantity);
        if(salePriceSubListDepartment.isEmpty()) {
            return null;
        }
        return salePriceSubListDepartment;
    }

    @Override
    public List<SalePrice> getSalePriceListMatvalue(
            Matvalue matvalue, int start, int size) {

        String salePriceListMatvalueSelectStatementSql =
                "SELECT refpricesofmatvalues.ID, " +
                        "refmatvalues.Code AS MatCode, " +
                        "refmatvalues.Name AS MatName, " +
                        "refunitsofmsrs.Code AS MsrCode, " +
                        "refunitsofmsrs.ShortName AS MsrShortName, " +
                        "refunitsofmsrs.LongName AS MsrLongName, " +
                        "refgroups.Code AS GrpCode, " +
                        "refgroups.Name AS GrpName, " +
                        "refgroups.EnhancedName AS GrpEnhancedName, " +
                        "refdepartments.Code AS DptCode, " +
                        "refdepartments.Name AS DptName, " +
                        "refdepartments.Street AS DptStreet, " +
                        "refdepartments.Pincode AS DptPincode, " +
                        "refdepartments.City AS DptCity, " +
                        "refdepartments.State AS DptState, " +
                        "refdepartments.Country AS DptCountry, " +
                        "refdepartments.Phones AS DptPhones, " +
                        "refdepartments.Faxes AS DptFaxes, " +
                        "refdepartments.WebSite AS DptWebSite, " +
                        "refdepartments.EmailAddress AS DptEmailAddress, " +
                        "refdepartments.AdditionalInformation AS DptAdditionalInformation, " +
                        "refcurrencies.Code AS CurCode, " +
                        "refcurrencies.ShortName AS CurShortName, " +
                        "refcurrencies.LongName AS CurLongName, " +
                        "refcurrencies.Country AS CurCountry, " +
                        "refcurrencies.AdditionalInformation AS CurAdditionalInformation, " +
                        "refpricesofmatvalues.Date, " +
                        "refpricesofmatvalues.Price, " +
                        "refpricesofmatvalues.Quantity " +
                        "FROM refpricesofmatvalues " +
                        "INNER JOIN refcurrencies " +
                        "ON refpricesofmatvalues.CurrencyCode = refcurrencies.Code " +
                        "INNER JOIN refdepartments " +
                        "ON refpricesofmatvalues.DepartmentCode = refdepartments.Code " +
                        "INNER JOIN refmatvalues " +
                        "ON refpricesofmatvalues.MatvalueCode = refmatvalues.Code " +
                        "INNER JOIN refunitsofmsrs " +
                        "ON refmatvalues.UnitofmsrCode = refunitsofmsrs.Code " +
                        "INNER JOIN refgroups " +
                        "ON refmatvalues.GroupCode = refgroups.Code " +
                        "WHERE refpricesofmatvalues.MatvalueCode = :matvalueCodeParameter " +
                        "ORDER BY refpricesofmatvalues.DepartmentCode ASC, " +
                        "refpricesofmatvalues.MatvalueCode ASC, " +
                        "refpricesofmatvalues.Date DESC, " +
                        "refpricesofmatvalues.ID DESC";
        List<SalePrice> salePriceListMatvalue = this.namedParameterJdbcTemplate
                .query(salePriceListMatvalueSelectStatementSql,
                        new MapSqlParameterSource("matvalueCodeParameter", matvalue.getCode()),
                        new SalePriceMapper());
        if (salePriceListMatvalue == null || salePriceListMatvalue.isEmpty()) {
            return null;
        }

        int first = Functions.calculateFirst(start, size, salePriceListMatvalue.size());
        int quantity = Functions.calculateQuantity(start, size, salePriceListMatvalue.size());
        List<SalePrice> salePriceSubListMatvalue =
                salePriceListMatvalue.subList(first, quantity);
        if(salePriceSubListMatvalue.isEmpty()) {
            return null;
        }
        return salePriceSubListMatvalue;
    }
}