package net.bsoftlab.dao.template.jpa;

import net.bsoftlab.dao.DepartmentDao;
import net.bsoftlab.model.Department;
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
public class DepartmentDaoJpaTemplate implements DepartmentDao {

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
    public void deleteDepartment(Department department) {
        this.entityManager.remove(department);
        this.entityManager.flush();
    }

    @Override
    public void insertDepartment(Department department) {
        this.entityManager.persist(department);
        this.entityManager.flush();
    }

    @Override
    public void updateDepartment(Department department) {
        this.entityManager.merge(department);
        this.entityManager.flush();
    }

    @Override
    public Department getDepartment(String code) {
        return this.entityManager.find(Department.class, code);
    }

    @Override
    public List<Department> getDepartmentList(int start, int size) {
        String departmentListSelectStatementJpql =
                "select department " +
                        "from Department as department " +
                        "order by department.code asc";
        TypedQuery<Department> departmentListSelectTypedQueryJpql = this.entityManager
                .createQuery(departmentListSelectStatementJpql, Department.class);
        List<Department> departmentList = departmentListSelectTypedQueryJpql.getResultList();
        if(departmentList == null || departmentList.isEmpty()) {
            return null;
        }
        int first = Functions.calculateFirst(start, size, departmentList.size());
        int quantity = Functions.calculateQuantity(start, size, departmentList.size());
        List<Department> departmentSubList = departmentList.subList(first, quantity);
        if(departmentSubList.isEmpty()) {
            return null;
        }
        return departmentSubList;
    }
}