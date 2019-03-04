package net.bsoftlab.dao.template.jpa;

import net.bsoftlab.dao.MatvalueDao;
import net.bsoftlab.model.Group;
import net.bsoftlab.model.Matvalue;
import net.bsoftlab.model.Unitofmsr;
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

import java.util.List;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MatvalueDaoJpaTemplate implements MatvalueDao {

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
    public void deleteMatvalue(Matvalue matvalue) {
        this.entityManager.remove(matvalue);
        this.entityManager.flush();
    }

    @Override
    public void insertMatvalue(Matvalue matvalue) {
        this.entityManager.persist(matvalue);
        this.entityManager.flush();
    }

    @Override
    public void updateMatvalue(Matvalue matvalue) {
        Matvalue matvaluePersistent = this.getMatvalue(matvalue.getCode());
        matvaluePersistent.setCode(matvalue.getCode());
        matvaluePersistent.setName(matvalue.getName());
        matvaluePersistent.setUnitofmsr(matvalue.getUnitofmsr());
        matvaluePersistent.setGroup(matvalue.getGroup());
        this.entityManager.merge(matvaluePersistent);
        this.entityManager.flush();
    }

    @Override
    public boolean existMatvalue(Group group) {
        try {
            String matvalueSelectStatementJpql =
                    "select matvalue " +
                            "from Matvalue as matvalue " +
                            "where matvalue.group = :groupParameter";
            TypedQuery<Matvalue> matvalueSelectQueryJpql = this.entityManager
                    .createQuery(matvalueSelectStatementJpql, Matvalue.class)
                    .setParameter("groupParameter", group);
            matvalueSelectQueryJpql.setMaxResults(1).getSingleResult();
            return true;
        } catch (NoResultException noResultException) {
            return false;
        }
    }

    @Override
    public boolean existMatvalue(Unitofmsr unitofmsr) {
        try {
            String matvalueSelectStatementJpql =
                    "select matvalue " +
                            "from Matvalue as matvalue " +
                            "where matvalue.unitofmsr = :unitofmsrParameter";
            TypedQuery<Matvalue> matvalueSelectQueryJpql = this.entityManager
                    .createQuery(matvalueSelectStatementJpql, Matvalue.class)
                    .setParameter("unitofmsrParameter", unitofmsr);
            matvalueSelectQueryJpql.setMaxResults(1).getSingleResult();
            return true;
        } catch (NoResultException noResultException) {
            return false;
        }
    }

    @Override
    public Matvalue getMatvalue(String code) {
        return this.entityManager.find(Matvalue.class, code);
    }

    @Override
    public List<Matvalue> getMatvalueList(int start, int size) {
        String matvalueListSelectStatementJpql =
                "select matvalue " +
                        "from Matvalue as matvalue " +
                        "order by matvalue.code asc";
        TypedQuery<Matvalue> matvalueListSelectTypedQueryJpql = this.entityManager
                .createQuery(matvalueListSelectStatementJpql, Matvalue.class);
        List<Matvalue> matvalueList = matvalueListSelectTypedQueryJpql.getResultList();
        if(matvalueList == null || matvalueList.isEmpty()) {
            return null;
        }
        int first = Functions.calculateFirst(start, size, matvalueList.size());
        int quantity = Functions.calculateQuantity(start, size, matvalueList.size());
        List<Matvalue> matvalueSubList = matvalueList.subList(first, quantity);
        if(matvalueSubList.isEmpty()) {
            return null;
        }
        return matvalueSubList;
    }

    @Override
    public List<Matvalue> getMatvalueListGroup(Group group, int start, int size) {
        String matvalueListGroupSelectStatementJpql =
                "select matvalue " +
                        "from Matvalue as matvalue " +
                        "where matvalue.group = :groupParameter " +
                        "order by matvalue.code asc";
        TypedQuery<Matvalue> matvalueListGroupSelectTypedQueryJpql = this.entityManager
                .createQuery(matvalueListGroupSelectStatementJpql, Matvalue.class)
                .setParameter("groupParameter", group);
        List<Matvalue> matvalueListGroup = matvalueListGroupSelectTypedQueryJpql.getResultList();
        if(matvalueListGroup == null || matvalueListGroup.isEmpty()) {
            return null;
        }
        int first = Functions.calculateFirst(start, size, matvalueListGroup.size());
        int quantity = Functions.calculateQuantity(start, size, matvalueListGroup.size());
        List<Matvalue> matvalueSubListGroup = matvalueListGroup.subList(first, quantity);
        if(matvalueSubListGroup.isEmpty()) {
            return null;
        }
        return matvalueSubListGroup;
    }

    @Override
    public List<Matvalue> getMatvalueListUnitofmsr(Unitofmsr unitofmsr, int start, int size) {
        String matvalueListUnitofmsrSelectStatementJpql =
                "select matvalue " +
                        "from Matvalue as matvalue " +
                        "where matvalue.unitofmsr = :unitofmsrParameter " +
                        "order by matvalue.code asc";
        TypedQuery<Matvalue> matvalueListUnitofmsrSelectTypedQueryJpql = this.entityManager
                .createQuery(matvalueListUnitofmsrSelectStatementJpql, Matvalue.class)
                .setParameter("unitofmsrParameter", unitofmsr);
        List<Matvalue> matvalueListUnitofmsr =
                matvalueListUnitofmsrSelectTypedQueryJpql.getResultList();
        if(matvalueListUnitofmsr == null || matvalueListUnitofmsr.isEmpty()) {
            return null;
        }
        int first = Functions.calculateFirst(start, size, matvalueListUnitofmsr.size());
        int quantity = Functions.calculateQuantity(start, size, matvalueListUnitofmsr.size());
        List<Matvalue> matvalueSubListUnitofmsr = matvalueListUnitofmsr.subList(first, quantity);
        if(matvalueSubListUnitofmsr.isEmpty()) {
            return null;
        }
        return matvalueSubListUnitofmsr;
    }
}