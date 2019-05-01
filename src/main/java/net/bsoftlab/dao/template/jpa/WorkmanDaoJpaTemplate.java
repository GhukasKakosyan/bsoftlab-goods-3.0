package net.bsoftlab.dao.template.jpa;

import net.bsoftlab.dao.api.WorkmanDao;
import net.bsoftlab.model.Workman;

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
public class WorkmanDaoJpaTemplate implements WorkmanDao {

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
    public void deleteWorkman(Workman workman) {
        this.entityManager.remove(workman);
        this.entityManager.flush();
    }

    @Override
    public void insertWorkman(Workman workman) {
        this.entityManager.persist(workman);
        this.entityManager.flush();
    }

    @Override
    public void updateWorkman(Workman workman) {
        Workman workmanPersistent = this.getWorkman(workman.getID());
        workmanPersistent.setID(workman.getID());
        workmanPersistent.setName(workman.getName());
        workmanPersistent.setPassword(workman.getPassword());
        workmanPersistent.setFirstName(workman.getFirstName());
        workmanPersistent.setLastName(workman.getLastName());
        workmanPersistent.setPhones(workman.getPhones());
        workmanPersistent.setAddress(workman.getAddress());
        workmanPersistent.getRoles().clear();
        workmanPersistent.getRoles().addAll(workman.getRoles());
        this.entityManager.merge(workmanPersistent);
        this.entityManager.flush();
    }

    @Override
    public Workman getWorkman(Integer ID) {
        return this.entityManager.find(Workman.class, ID);
    }

    @Override
    public Workman getWorkman(Integer ID, String name) {
        try {
            String workmanSelectStatementJpql =
                    "select workman " +
                            "from Workman as workman " +
                            "where workman.name = :nameParameter " +
                            "and not workman.ID = :IDParameter";
            TypedQuery<Workman> workmanSelectTypedQueryJpql = this.entityManager
                    .createQuery(workmanSelectStatementJpql, Workman.class)
                    .setParameter("nameParameter", name)
                    .setParameter("IDParameter", ID);
            return workmanSelectTypedQueryJpql.getSingleResult();

        } catch (NoResultException noResultException) {
            return null;
        }
    }

    @Override
    public List<Workman> getWorkmanList() {
        String workmanListSelectStatementJpql =
                "select workman " +
                        "from Workman as workman " +
                        "order by workman.ID asc";
        TypedQuery<Workman> workmanListSelectTypedQueryJpql = this.entityManager
                .createQuery(workmanListSelectStatementJpql, Workman.class);
        return workmanListSelectTypedQueryJpql.getResultList();
    }
}