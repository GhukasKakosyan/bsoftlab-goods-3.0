package net.bsoftlab.dao.template.jpa;

import net.bsoftlab.dao.PermissionDao;
import net.bsoftlab.model.Permission;

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
public class PermissionDaoJpaTemplate implements PermissionDao {

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
    public Permission getPermission(Integer ID) {
        return this.entityManager.find(Permission.class, ID);
    }

    @Override
    public Permission getPermission(String name) {
        try {
            String permissionSelectStatementJpql =
                    "select permission " +
                            "from Permission as permission " +
                            "where permission.name = :nameParameter";
            TypedQuery<Permission> permissionSelectTypedQueryJpql = this.entityManager
                    .createQuery(permissionSelectStatementJpql, Permission.class)
                    .setParameter("nameParameter", name);
            return permissionSelectTypedQueryJpql.getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    @Override
    public List<Permission> getPermissionList() {
        String permissionListSelectStatementJpql =
                "select permission " +
                        "from Permission as permission " +
                        "order by permission.ID asc";
        TypedQuery<Permission> permissionListSelectTypedQueryJpql = this.entityManager
                .createQuery(permissionListSelectStatementJpql, Permission.class);
        return permissionListSelectTypedQueryJpql.getResultList();
    }
}
