package net.bsoftlab.dao.template.jpa;

import net.bsoftlab.dao.api.CurrencyDao;
import net.bsoftlab.model.Currency;
import net.bsoftlab.utility.Functions;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.util.List;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CurrencyDaoJpaTemplate implements CurrencyDao {

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
    public void deleteCurrency(Currency currency) {
        this.entityManager.remove(currency);
        this.entityManager.flush();
    }

    @Override
    public void insertCurrency(Currency currency) {
        this.entityManager.persist(currency);
        this.entityManager.flush();
    }

    @Override
    public void updateCurrency(Currency currency) {
        this.entityManager.merge(currency);
        this.entityManager.flush();
    }

    @Override
    public Currency getCurrency(String code) {
        return this.entityManager.find(Currency.class, code);
    }

    @Override
    public List<Currency> getCurrencyList(int start, int size) {
        String currencyListSelectStatementJpql =
                "select currency " +
                        "from Currency as currency " +
                        "order by currency.code asc";
        TypedQuery<Currency> currencyListSelectTypedQueryJpql = this.entityManager
                .createQuery(currencyListSelectStatementJpql, Currency.class);
        List<Currency> currencyList = currencyListSelectTypedQueryJpql.getResultList();
        if(currencyList == null || currencyList.isEmpty()) {
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