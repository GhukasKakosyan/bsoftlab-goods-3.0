package net.bsoftlab.dao.template.jdbc.core;

import net.bsoftlab.dao.MatvalueDao;
import net.bsoftlab.dao.template.jdbc.mapper.MatvalueMapper;
import net.bsoftlab.dao.template.jdbc.mapper.SalePriceMapper;
import net.bsoftlab.model.Group;
import net.bsoftlab.model.Matvalue;
import net.bsoftlab.model.SalePrice;
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

import java.util.HashSet;
import java.util.List;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MatvalueDaoJdbcTemplate implements MatvalueDao {

    private JdbcTemplate jdbcTemplate;
    private UtilityFunctions utilityFunctions = null;

    @Autowired
    public MatvalueDaoJdbcTemplate(
            JdbcTemplate jdbcTemplate){
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
    public void deleteMatvalue(Matvalue matvalue) {
        String matvalueDeleteStatementSql =
                "DELETE FROM refmatvalues " +
                        "WHERE refmatvalues.Code = ?";
        this.jdbcTemplate.update(matvalueDeleteStatementSql, matvalue.getCode());
    }

    @Override
    public void insertMatvalue(Matvalue matvalue) {
        String matvalueInsertStatementSql =
                "INSERT INTO refmatvalues " +
                        "(Code, Name, UnitofmsrCode, GroupCode) " +
                        "VALUES (?, ?, ?, ?)";
        this.jdbcTemplate.update(matvalueInsertStatementSql,
                matvalue.getCode(),
                matvalue.getName(),
                matvalue.getUnitofmsr().getCode(),
                matvalue.getGroup().getCode());
    }

    @Override
    public void updateMatvalue(Matvalue matvalue) {
        String matvalueUpdateStatementSql =
                "UPDATE refmatvalues " +
                        "SET refmatvalues.Name = ?, " +
                        "refmatvalues.UnitofmsrCode = ?, " +
                        "refmatvalues.GroupCode = ? " +
                        "WHERE refmatvalues.Code = ?";
        this.jdbcTemplate.update(matvalueUpdateStatementSql,
                matvalue.getName(),
                matvalue.getUnitofmsr().getCode(),
                matvalue.getGroup().getCode(),
                matvalue.getCode());
    }

    @Override
    public boolean existMatvalue(Group group) {
        try {
            String existMatvalueGroupSelectStatementSql =
                    "SELECT GroupCode " +
                            "FROM refmatvalues " +
                            "WHERE GroupCode = ? LIMIT 1";
            this.jdbcTemplate.queryForObject(
                    existMatvalueGroupSelectStatementSql,
                    new Object[]{group.getCode()}, String.class);
            return true;
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return false;
        }
    }

    @Override
    public boolean existMatvalue(Unitofmsr unitofmsr) {
        try {
            String existMatvalueUnitofmsrSelectStatementSql =
                    "SELECT UnitofmsrCode " +
                            "FROM refmatvalues " +
                            "WHERE UnitofmsrCode = ? LIMIT 1";
            this.jdbcTemplate.queryForObject(
                    existMatvalueUnitofmsrSelectStatementSql,
                    new Object[]{unitofmsr.getCode()}, String.class);
            return true;
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return false;
        }
    }

    @Override
    public Matvalue getMatvalue(String code) {

        Matvalue matvaluePersistent;
        try {
            String matvalueSelectStatementSql =
                    "SELECT refmatvalues.Code, " +
                            "refmatvalues.Name, " +
                            "refunitsofmsrs.Code AS MsrCode, " +
                            "refunitsofmsrs.ShortName AS MsrShortName, " +
                            "refunitsofmsrs.LongName AS MsrLongName, " +
                            "refgroups.Code AS GrpCode, " +
                            "refgroups.Name AS GrpName, " +
                            "refgroups.EnhancedName AS GrpEnhancedName " +
                            "FROM refunitsofmsrs, refgroups, refmatvalues " +
                            "WHERE refmatvalues.UnitofmsrCode = refunitsofmsrs.Code " +
                            "AND refmatvalues.GroupCode = refgroups.Code " +
                            "AND refmatvalues.Code = ?";
            matvaluePersistent = this.jdbcTemplate.queryForObject(
                    matvalueSelectStatementSql, new Object[]{code}, new MatvalueMapper());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return null;
        }

        String salePriceSetMatvalueSelectStatementSql =
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
                        "FROM refcurrencies, refdepartments, refunitsofmsrs, refgroups, refmatvalues, refpricesofmatvalues " +
                        "WHERE refpricesofmatvalues.MatvalueCode = ?" +
                        "AND refpricesofmatvalues.MatvalueCode = refmatvalues.Code " +
                        "AND refpricesofmatvalues.DepartmentCode = refdepartments.Code " +
                        "AND refpricesofmatvalues.CurrencyCode = refcurrencies.Code " +
                        "AND refmatvalues.UnitofmsrCode = refunitsofmsrs.Code " +
                        "AND refmatvalues.GroupCode = refgroups.Code " +
                        "ORDER BY refpricesofmatvalues.Date DESC, " +
                        "refpricesofmatvalues.ID DESC";
        List<SalePrice> salePriceListMatvalue = this.jdbcTemplate.query(
                salePriceSetMatvalueSelectStatementSql,
                new Object[]{code}, new SalePriceMapper());
        if (salePriceListMatvalue == null || salePriceListMatvalue.isEmpty()) {
            return matvaluePersistent;
        }
        matvaluePersistent.setSalePriceSet(new HashSet<>());
        matvaluePersistent.getSalePriceSet().addAll(salePriceListMatvalue);
        return matvaluePersistent;
    }

    @Override
    public List<Matvalue> getMatvalueList(int start, int size) {
        String matvalueListSelectStatementSql =
                "SELECT refmatvalues.Code, " +
                        "refmatvalues.Name, " +
                        "refunitsofmsrs.Code AS MsrCode, " +
                        "refunitsofmsrs.ShortName AS MsrShortName, " +
                        "refunitsofmsrs.LongName AS MsrLongName, " +
                        "refgroups.Code AS GrpCode, " +
                        "refgroups.Name AS GrpName, " +
                        "refgroups.EnhancedName AS GrpEnhancedName " +
                        "FROM refunitsofmsrs, refgroups, refmatvalues " +
                        "WHERE refmatvalues.UnitofmsrCode = refunitsofmsrs.Code " +
                        "AND refmatvalues.GroupCode = refgroups.Code " +
                        "ORDER BY refmatvalues.Code ASC";
        List<Matvalue> matvalueList = this.jdbcTemplate.query(
                matvalueListSelectStatementSql, new MatvalueMapper());
        if (matvalueList == null || matvalueList.isEmpty()) {
            return null;
        }

        int first = this.utilityFunctions.calculateFirst(start, size, matvalueList.size());
        int quantity = this.utilityFunctions.calculateQuantity(start, size, matvalueList.size());
        List<Matvalue> matvalueSubList = matvalueList.subList(first, quantity);
        if(matvalueSubList.isEmpty()) {
            return null;
        }

        String salePriceSetSelectStatementSql =
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
                        "FROM refcurrencies, refdepartments, refunitsofmsrs, refgroups, refmatvalues, refpricesofmatvalues " +
                        "WHERE refpricesofmatvalues.MatvalueCode = refmatvalues.Code " +
                        "AND refpricesofmatvalues.DepartmentCode = refdepartments.Code " +
                        "AND refpricesofmatvalues.CurrencyCode = refcurrencies.Code " +
                        "AND refmatvalues.UnitofmsrCode = refunitsofmsrs.Code " +
                        "AND refmatvalues.GroupCode = refgroups.Code " +
                        "ORDER BY refpricesofmatvalues.MatvalueCode ASC, " +
                        "refpricesofmatvalues.Date DESC, refpricesofmatvalues.ID DESC";
        List<SalePrice> salePriceList = this.jdbcTemplate.query(
                salePriceSetSelectStatementSql, new SalePriceMapper());
        if (salePriceList == null || salePriceList.isEmpty()) {
            return matvalueSubList;
        }

        for(Matvalue matvalue : matvalueSubList) {
            matvalue.setSalePriceSet(new HashSet<>());
            salePriceList.stream().filter(salePrice -> salePrice.getMatvalue().getCode()
                    .equals(matvalue.getCode())).forEach(salePrice ->
                    matvalue.getSalePriceSet().add(salePrice));
        }
        return matvalueSubList;
    }

    @Override
    public List<Matvalue> getMatvalueListGroup(Group group, int start, int size) {
        String matvalueListGroupSelectStatementSql =
                "SELECT refmatvalues.Code, " +
                        "refmatvalues.Name, " +
                        "refunitsofmsrs.Code AS MsrCode, " +
                        "refunitsofmsrs.ShortName AS MsrShortName, " +
                        "refunitsofmsrs.LongName AS MsrLongName, " +
                        "refgroups.Code AS GrpCode, " +
                        "refgroups.Name AS GrpName, " +
                        "refgroups.EnhancedName AS GrpEnhancedName " +
                        "FROM refunitsofmsrs, refgroups, refmatvalues " +
                        "WHERE refmatvalues.UnitofmsrCode = refunitsofmsrs.Code " +
                        "AND refmatvalues.GroupCode = refgroups.Code " +
                        "AND refmatvalues.GroupCode = ?" +
                        "ORDER BY refmatvalues.Code ASC";
        List<Matvalue> matvalueListGroup = this.jdbcTemplate.query(
                matvalueListGroupSelectStatementSql,
                new Object[]{group.getCode()}, new MatvalueMapper());
        if (matvalueListGroup == null || matvalueListGroup.isEmpty()) {
            return null;
        }

        int first = this.utilityFunctions.calculateFirst(start, size, matvalueListGroup.size());
        int quantity = this.utilityFunctions.calculateQuantity(start, size, matvalueListGroup.size());
        List<Matvalue> matvalueSubListGroup = matvalueListGroup.subList(first, quantity);
        if(matvalueSubListGroup.isEmpty()) {
            return null;
        }

        String salePriceSetGroupSelectStatementSql =
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
                        "FROM refcurrencies, refdepartments, refunitsofmsrs, refgroups, refmatvalues, refpricesofmatvalues " +
                        "WHERE refpricesofmatvalues.MatvalueCode = refmatvalues.Code " +
                        "AND refpricesofmatvalues.DepartmentCode = refdepartments.Code " +
                        "AND refpricesofmatvalues.CurrencyCode = refcurrencies.Code " +
                        "AND refmatvalues.UnitofmsrCode = refunitsofmsrs.Code " +
                        "AND refmatvalues.GroupCode = refgroups.Code " +
                        "AND refmatvalues.GroupCode = ? " +
                        "ORDER BY refpricesofmatvalues.MatvalueCode ASC, " +
                        "refpricesofmatvalues.Date DESC, refpricesofmatvalues.ID DESC";
        List<SalePrice> salePriceListGroup = this.jdbcTemplate.query(
                salePriceSetGroupSelectStatementSql,
                new Object[]{group.getCode()}, new SalePriceMapper());
        if (salePriceListGroup == null || salePriceListGroup.isEmpty()) {
            return matvalueSubListGroup;
        }

        for(Matvalue matvalue : matvalueSubListGroup) {
            matvalue.setSalePriceSet(new HashSet<>());
            salePriceListGroup.stream().filter(salePrice -> matvalue
                    .getCode().equals(salePrice.getMatvalue().getCode()))
                    .forEach(salePrice -> matvalue.getSalePriceSet().add(salePrice));
        }
        return matvalueSubListGroup;
    }

    @Override
    public List<Matvalue> getMatvalueListUnitofmsr(Unitofmsr unitofmsr, int start, int size) {
        String matvalueListUnitofmsrSelectStatementSql =
                "SELECT refmatvalues.Code, " +
                        "refmatvalues.Name, " +
                        "refunitsofmsrs.Code AS MsrCode, " +
                        "refunitsofmsrs.ShortName AS MsrShortName, " +
                        "refunitsofmsrs.LongName AS MsrLongName, " +
                        "refgroups.Code AS GrpCode, " +
                        "refgroups.Name AS GrpName, " +
                        "refgroups.EnhancedName AS GrpEnhancedName " +
                        "FROM refunitsofmsrs, refgroups, refmatvalues " +
                        "WHERE refmatvalues.UnitofmsrCode = refunitsofmsrs.Code " +
                        "AND refmatvalues.GroupCode = refgroups.Code " +
                        "AND refmatvalues.UnitofmsrCode = ?" +
                        "ORDER BY refmatvalues.Code ASC";
        List<Matvalue> matvalueListUnitofmsr = this.jdbcTemplate.query(
                matvalueListUnitofmsrSelectStatementSql,
                new Object[]{unitofmsr.getCode()}, new MatvalueMapper());
        if (matvalueListUnitofmsr == null || matvalueListUnitofmsr.isEmpty()) {
            return null;
        }

        int first = this.utilityFunctions.calculateFirst(start, size, matvalueListUnitofmsr.size());
        int quantity = this.utilityFunctions.calculateQuantity(start, size, matvalueListUnitofmsr.size());
        List<Matvalue> matvalueSubListUnitofmsr = matvalueListUnitofmsr.subList(first, quantity);
        if(matvalueSubListUnitofmsr.isEmpty()) {
            return null;
        }

        String salePriceSetUnitofmsrSelectStatementSql =
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
                        "FROM refcurrencies, refdepartments, refunitsofmsrs, refgroups, refmatvalues, refpricesofmatvalues " +
                        "WHERE refpricesofmatvalues.MatvalueCode = refmatvalues.Code " +
                        "AND refpricesofmatvalues.DepartmentCode = refdepartments.Code " +
                        "AND refpricesofmatvalues.CurrencyCode = refcurrencies.Code " +
                        "AND refmatvalues.UnitofmsrCode = refunitsofmsrs.Code " +
                        "AND refmatvalues.GroupCode = refgroups.Code " +
                        "AND refmatvalues.UnitofmsrCode = ? " +
                        "ORDER BY refpricesofmatvalues.MatvalueCode ASC, " +
                        "refpricesofmatvalues.Date DESC, refpricesofmatvalues.ID DESC";
        List<SalePrice> salePriceListUnitofmsr = this.jdbcTemplate.query(
                salePriceSetUnitofmsrSelectStatementSql,
                new Object[]{unitofmsr.getCode()}, new SalePriceMapper());
        if (salePriceListUnitofmsr == null || salePriceListUnitofmsr.isEmpty()) {
            return matvalueSubListUnitofmsr;
        }

        for (Matvalue matvalue : matvalueSubListUnitofmsr) {
            matvalue.setSalePriceSet(new HashSet<>());
            salePriceListUnitofmsr.stream().filter((SalePrice salePrice) -> matvalue
                    .getCode().equals(salePrice.getMatvalue().getCode()))
                    .forEach(salePrice -> matvalue.getSalePriceSet().add(salePrice));
        }
        return matvalueSubListUnitofmsr;
    }
}