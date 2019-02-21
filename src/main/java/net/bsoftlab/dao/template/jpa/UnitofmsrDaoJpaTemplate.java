package net.bsoftlab.dao.template.jpa;

import net.bsoftlab.dao.UnitofmsrDao;
import net.bsoftlab.model.Unitofmsr;
import net.bsoftlab.utility.UtilityFunctions;

import org.springframework.beans.factory.annotation.Autowired;
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
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UnitofmsrDaoJpaTemplate implements UnitofmsrDao {

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
    public void deleteUnitofmsr(Unitofmsr unitofmsr) {
        this.entityManager.remove(unitofmsr);
        this.entityManager.flush();
    }

    @Override
    public void insertUnitofmsr(Unitofmsr unitofmsr) {
        this.entityManager.persist(unitofmsr);
        this.entityManager.flush();
    }

    @Override
    public void updateUnitofmsr(Unitofmsr unitofmsr) {
        this.entityManager.merge(unitofmsr);
        this.entityManager.flush();
    }

    @Override
    public Unitofmsr getUnitofmsr(String code) {
        return this.entityManager.find(Unitofmsr.class, code);
    }

    @Override
    public List<Unitofmsr> getUnitofmsrList(int start, int size) {
        String unitofmsrListSelectStatementJpql =
                "select unitofmsr " +
                        "from Unitofmsr as unitofmsr " +
                        "order by unitofmsr.code asc";
        TypedQuery<Unitofmsr> unitofmsrListSelectTypedQueryJpql = this.entityManager
                .createQuery(unitofmsrListSelectStatementJpql, Unitofmsr.class);
        List<Unitofmsr> unitofmsrList = unitofmsrListSelectTypedQueryJpql.getResultList();
        if(unitofmsrList == null || unitofmsrList.isEmpty()) {
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
