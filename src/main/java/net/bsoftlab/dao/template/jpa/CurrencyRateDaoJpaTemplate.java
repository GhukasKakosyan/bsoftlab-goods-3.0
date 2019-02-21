package net.bsoftlab.dao.template.jpa;

import net.bsoftlab.dao.CurrencyRateDao;
import net.bsoftlab.model.Currency;
import net.bsoftlab.model.CurrencyRate;
import net.bsoftlab.utility.UtilityFunctions;

import org.springframework.beans.factory.annotation.Autowired;
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
public class CurrencyRateDaoJpaTemplate implements CurrencyRateDao {

    private EntityManager entityManager = null;
    private UtilityFunctions utilityFunctions = null;

    @PreDestroy
    public void destroy() {}
    @PostConstruct
    public void init() {}

    @PersistenceContext
    public void setEntityManager(
            EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Autowired
    public void setUtilityFunctions(
            UtilityFunctions utilityFunctions) {
        this.utilityFunctions = utilityFunctions;
    }

    @Override
    public void deleteCurrencyRate(CurrencyRate currencyRate) {
        this.entityManager.remove(currencyRate);
        this.entityManager.flush();
    }

    @Override
    public void insertCurrencyRate(CurrencyRate currencyRate) {
        this.entityManager.persist(currencyRate);
        this.entityManager.flush();
    }

    @Override
    public void updateCurrencyRate(CurrencyRate currencyRate) {
        this.entityManager.merge(currencyRate);
        this.entityManager.flush();
    }

    @Override
    public boolean existCurrencyRate(Currency currency) {
        try {
            String currencyRateSelectStatementJpql =
                    "select currencyRate " +
                            "from CurrencyRate as currencyRate " +
                            "where currencyRate.currency = :currencyParameter";
            TypedQuery<CurrencyRate> currencyRateSelectQueryJpql = this.entityManager
                    .createQuery(currencyRateSelectStatementJpql, CurrencyRate.class)
                    .setParameter("currencyParameter", currency);
            currencyRateSelectQueryJpql.setMaxResults(1).getSingleResult();
            return true;
        } catch (NoResultException noResultExceptionCurrencyRate) {
            return false;
        }
    }

    @Override
    public CurrencyRate getCurrencyRate(Integer ID) {
        return this.entityManager.find(CurrencyRate.class, ID);
    }

    @Override
    public CurrencyRate getCurrencyRate(Currency currency, Date date, Integer ID) {
        try {
            String currencyRateSelectStatementJpql =
                    "select currencyRate " +
                            "from CurrencyRate as currencyRate " +
                            "where not currencyRate.ID = :idParameter " +
                            "and currencyRate.date = :dateParameter " +
                            "and currencyRate.currency = :currencyParameter";
            TypedQuery<CurrencyRate> currencyRateSelectTypedQueryJpql = this.entityManager
                    .createQuery(currencyRateSelectStatementJpql, CurrencyRate.class)
                    .setParameter("idParameter", ID)
                    .setParameter("dateParameter", date)
                    .setParameter("currencyParameter", currency);
            return currencyRateSelectTypedQueryJpql.getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    @Override
    public List<CurrencyRate> getCurrencyRateList(int start, int size) {
        String currencyRateListSelectStatementJpql =
                "select currencyRate " +
                        "from CurrencyRate as currencyRate " +
                        "order by currencyRate.date desc, currencyRate.ID desc";
        TypedQuery<CurrencyRate> currencyRateListSelectJpql = this.entityManager
                .createQuery(currencyRateListSelectStatementJpql, CurrencyRate.class);
        List<CurrencyRate> currencyRateList = currencyRateListSelectJpql.getResultList();
        if(currencyRateList == null || currencyRateList.isEmpty()) {
            return null;
        }
        int first = this.utilityFunctions.calculateFirst(start, size, currencyRateList.size());
        int quantity = this.utilityFunctions.calculateQuantity(start, size, currencyRateList.size());
        List<CurrencyRate> currencyRateSubList = currencyRateList.subList(first, quantity);
        if(currencyRateSubList.isEmpty()) {
            return null;
        }
        return currencyRateSubList;
    }

    @Override
    public List<CurrencyRate> getCurrencyRateList(Currency currency, int start, int size) {
        String currencyRateListSelectStatementJpql =
                "select currencyRate " +
                        "from CurrencyRate as currencyRate " +
                        "where currencyRate.currency = :currencyParameter " +
                        "order by currencyRate.date desc, currencyRate.ID desc";
        TypedQuery<CurrencyRate> currencyRateListSelectTypedQueryJpql = this.entityManager
                .createQuery(currencyRateListSelectStatementJpql, CurrencyRate.class)
                .setParameter("currencyParameter", currency);
        List<CurrencyRate> currencyRateList =
                currencyRateListSelectTypedQueryJpql.getResultList();
        if(currencyRateList == null || currencyRateList.isEmpty()) {
            return null;
        }
        int first = this.utilityFunctions.calculateFirst(start, size, currencyRateList.size());
        int quantity = this.utilityFunctions.calculateQuantity(start, size, currencyRateList.size());
        List<CurrencyRate> currencyRateSubList = currencyRateList.subList(first, quantity);
        if(currencyRateSubList.isEmpty()) {
            return null;
        }
        return currencyRateSubList;
    }
}