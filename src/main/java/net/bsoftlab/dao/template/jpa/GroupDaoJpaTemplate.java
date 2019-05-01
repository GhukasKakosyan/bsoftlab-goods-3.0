package net.bsoftlab.dao.template.jpa;

import net.bsoftlab.dao.api.GroupDao;
import net.bsoftlab.model.Group;
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
public class GroupDaoJpaTemplate implements GroupDao {

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
    public void deleteGroup(Group group) {
        this.entityManager.remove(group);
        this.entityManager.flush();
    }

    @Override
    public void insertGroup(Group group) {
        this.entityManager.persist(group);
        this.entityManager.flush();
    }

    @Override
    public void updateGroup(Group group) {
        this.entityManager.merge(group);
        this.entityManager.flush();
    }

    @Override
    public Group getGroup(String code) {
        return this.entityManager.find(Group.class, code);
    }

    @Override
    public List<Group> getGroupList(int start, int size) {
        String groupListSelectStatementJpql =
                "select groupProduct " +
                        "from GroupProduct as groupProduct " +
                        "order by groupProduct.code asc";
        TypedQuery<Group> groupListSelectTypedQueryJpql = this.entityManager
                .createQuery(groupListSelectStatementJpql, Group.class);
        List<Group> groupList = groupListSelectTypedQueryJpql.getResultList();
        if(groupList == null || groupList.isEmpty()) {
            return null;
        }
        int first = Functions.calculateFirst(start, size, groupList.size());
        int quantity = Functions.calculateQuantity(start, size, groupList.size());
        List<Group> groupSubList = groupList.subList(first, quantity);
        if(groupSubList.isEmpty()) {
            return null;
        }
        return groupSubList;
    }
}