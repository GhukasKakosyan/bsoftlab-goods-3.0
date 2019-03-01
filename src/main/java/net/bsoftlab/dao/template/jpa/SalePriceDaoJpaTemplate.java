package net.bsoftlab.dao.template.jpa;

import net.bsoftlab.dao.SalePriceDao;
import net.bsoftlab.model.Currency;
import net.bsoftlab.model.Department;
import net.bsoftlab.model.Matvalue;
import net.bsoftlab.model.SalePrice;
import net.bsoftlab.utility.Functions;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.util.Date;
import java.util.List;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SalePriceDaoJpaTemplate implements SalePriceDao {

    private EntityManager entityManager = null;

    @PreDestroy
    public void destroy() {}
    @PostConstruct
    public void init() {}

    @PersistenceContext
    public void setEntityManager(
            EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void deleteSalePrice(SalePrice salePrice) {
        this.entityManager.remove(salePrice);
        this.entityManager.flush();
    }

    @Override
    public void insertSalePrice(SalePrice salePrice) {
        this.entityManager.persist(salePrice);
        this.entityManager.flush();
    }

    @Override
    public void updateSalePrice(SalePrice salePrice) {
        this.entityManager.merge(salePrice);
        this.entityManager.flush();
    }

    @Override
    public boolean existSalePrice(Currency currency) {
        try {
            String salePriceSelectStatementJpql =
                    "select salePrice " +
                            "from SalePrice as salePrice " +
                            "where salePrice.currency = :currencyParameter";
            TypedQuery<SalePrice> salePriceSelectQueryJpql = this.entityManager
                    .createQuery(salePriceSelectStatementJpql, SalePrice.class)
                    .setParameter("currencyParameter", currency);
            salePriceSelectQueryJpql.setMaxResults(1).getSingleResult();
            return true;
        } catch (NoResultException noResultException) {
            return false;
        }
    }

    @Override
    public boolean existSalePrice(Department department) {
        try {
            String salePriceSelectStatementJpql =
                    "select salePrice " +
                            "from SalePrice as salePrice " +
                            "where salePrice.department = :departmentParameter";
            TypedQuery<SalePrice> salePriceSelectQueryJpql = this.entityManager
                    .createQuery(salePriceSelectStatementJpql, SalePrice.class)
                    .setParameter("departmentParameter", department);
            salePriceSelectQueryJpql.setMaxResults(1).getSingleResult();
            return true;
        } catch (NoResultException noResultException) {
            return false;
        }
    }

    @Override
    public SalePrice getSalePrice(Integer ID) {
        return this.entityManager.find(SalePrice.class, ID);
    }

    @Override
    public SalePrice getSalePrice(
            Department department, Matvalue matvalue, Date date, Integer ID) {
        try {
            String salePriceSelectStatementJpql =
                    "select salePrice " +
                            "from SalePrice as salePrice " +
                            "where salePrice.date = :dateParameter " +
                            "and not salePrice.ID = :IDParameter " +
                            "and salePrice.department  = :departmentParameter " +
                            "and salePrice.matvalue = :matvalueParameter";
            TypedQuery<SalePrice> salePriceSelectTypedQueryJpql = this.entityManager
                    .createQuery(salePriceSelectStatementJpql, SalePrice.class)
                    .setParameter("dateParameter", date)
                    .setParameter("IDParameter", ID)
                    .setParameter("departmentParameter", department)
                    .setParameter("matvalueParameter", matvalue);
            return salePriceSelectTypedQueryJpql.getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }

    }

    @Override
    public List<SalePrice> getSalePriceList(int start, int size) {
        String salePriceListSelectStatementJpql =
                "select salePrice " +
                        "from SalePrice as salePrice " +
                        "order by salePrice.date desc, salePrice.ID desc";
        TypedQuery<SalePrice> salePriceListSelectTypedQueryJpql = this.entityManager
                .createQuery(salePriceListSelectStatementJpql, SalePrice.class);
        List<SalePrice> salePriceList = salePriceListSelectTypedQueryJpql.getResultList();
        if(salePriceList == null || salePriceList.isEmpty()) {
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

        String salePriceListCurrencySelectStatementJpql =
                "select salePrice " +
                        "from SalePrice as salePrice " +
                        "where salePrice.currency = :currencyParameter " +
                        "order by salePrice.date desc, salePrice.ID desc";
        TypedQuery<SalePrice> salePriceListCurrencySelectTypedQueryJpql = this.entityManager
                .createQuery(salePriceListCurrencySelectStatementJpql, SalePrice.class)
                .setParameter("currencyParameter", currency);
        List<SalePrice> salePriceListCurrency =
                salePriceListCurrencySelectTypedQueryJpql.getResultList();
        if(salePriceListCurrency == null || salePriceListCurrency.isEmpty()) {
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

        String salePriceListDepartmentSelectStatementJpql =
                "select salePrice " +
                        "from SalePrice as salePrice " +
                        "where salePrice.department = :departmentParameter " +
                        "order by salePrice.date desc, salePrice.ID desc";
        TypedQuery<SalePrice> salePriceListDepartmentSelectTypedQueryJpql = this.entityManager
                .createQuery(salePriceListDepartmentSelectStatementJpql, SalePrice.class)
                .setParameter("departmentParameter", department);
        List<SalePrice> salePriceListDepartment =
                salePriceListDepartmentSelectTypedQueryJpql.getResultList();
        if(salePriceListDepartment == null || salePriceListDepartment.isEmpty()) {
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

        String salePriceListMatvalueSelectStatementJpql =
                "select salePrice " +
                        "from SalePrice as salePrice " +
                        "where salePrice.matvalue = :matvalueParameter " +
                        "order by salePrice.date desc, salePrice.ID desc";
        TypedQuery<SalePrice> salePriceListMatvalueSelectTypedQueryJpql = this.entityManager
                .createQuery(salePriceListMatvalueSelectStatementJpql, SalePrice.class)
                .setParameter("matvalueParameter", matvalue);
        List<SalePrice> salePriceListMatvalue =
                salePriceListMatvalueSelectTypedQueryJpql.getResultList();
        if(salePriceListMatvalue == null || salePriceListMatvalue.isEmpty()) {
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