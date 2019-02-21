package net.bsoftlab.dao.template.jpa;

import net.bsoftlab.dao.RoleDao;
import net.bsoftlab.model.Role;

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
public class RoleDaoJpaTemplate implements RoleDao {

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
    public Role getRole(Integer ID) {
        return this.entityManager.find(Role.class, ID);
    }

    @Override
    public List<Role> getRoleList() {
        String roleListSelectStatementJpql =
                "select role " +
                        "from Role as role " +
                        "order by role.ID asc";
        TypedQuery<Role> roleListSelectTypedQueryJpql = this.entityManager
                .createQuery(roleListSelectStatementJpql, Role.class);
        return roleListSelectTypedQueryJpql.getResultList();
    }
}