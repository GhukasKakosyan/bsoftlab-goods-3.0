package net.bsoftlab.service.implement;

import net.bsoftlab.dao.SalePriceDao;
import net.bsoftlab.dao.DepartmentDao;
import net.bsoftlab.model.Department;
import net.bsoftlab.service.DepartmentService;
import net.bsoftlab.service.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.util.List;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DepartmentServiceImpl implements DepartmentService {

    private static final String DepartmentNotExistMessageCode = "department.not.exist";
    private static final String DepartmentPrimaryKeyInvalidMessageCode = "department.primaryKey.invalid";
    private static final String DepartmentUsedInSalePricesMessageCode = "department.used.in.salePrices";

    private DepartmentDao departmentDao = null;
    private SalePriceDao salePriceDao = null;

    @PreDestroy
    public void destroy(){}
    @PostConstruct
    public void init(){}

    @Autowired
    @Qualifier("departmentDaoJdbcTemplate")
    public void setDepartmentDao(
            DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }
    @Autowired
    @Qualifier("salePriceDaoJdbcTemplate")
    public void setSalePriceDao(
            SalePriceDao salePriceDao) {
        this.salePriceDao = salePriceDao;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class, timeout = 10)
    public void deleteDepartment(String code) throws ServiceException {
        Department departmentPersistent = this.getDepartment(code);
        if (departmentPersistent == null) {
            throw new ServiceException(DepartmentNotExistMessageCode);
        }
        if (this.salePriceDao.existSalePrice(departmentPersistent)) {
            throw new ServiceException(DepartmentUsedInSalePricesMessageCode);
        }
        this.departmentDao.deleteDepartment(departmentPersistent);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class, timeout = 10)
    public void insertDepartment(Department department) throws ServiceException {
        Department departmentPersistent = this.getDepartment(department.getCode());
        if(departmentPersistent != null) {
            throw new ServiceException(DepartmentPrimaryKeyInvalidMessageCode);
        }
        this.departmentDao.insertDepartment(department);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class, timeout = 10)
    public void updateDepartment(Department department) throws ServiceException {
        Department departmentPersistent = this.getDepartment(department.getCode());
        if(departmentPersistent == null) {
            throw new ServiceException(DepartmentNotExistMessageCode);
        }
        this.departmentDao.updateDepartment(department);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public Department getDepartment(String code) {
        return this.departmentDao.getDepartment(code);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public List<Department> getDepartmentList() {
        return this.departmentDao.getDepartmentList(0, 0);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public List<Department> getDepartmentList(int start, int size) {
        if(start > 0 && size > 0) {
            return this.departmentDao.getDepartmentList(start, size);
        } else if(start == 0 && size == 0) {
            return this.departmentDao.getDepartmentList(start, size);
        } else {
            return null;
        }
    }
}