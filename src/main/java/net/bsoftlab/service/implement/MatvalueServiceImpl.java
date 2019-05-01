package net.bsoftlab.service.implement;

import net.bsoftlab.dao.api.GroupDao;
import net.bsoftlab.dao.api.UnitofmsrDao;
import net.bsoftlab.dao.api.MatvalueDao;
import net.bsoftlab.model.Group;
import net.bsoftlab.model.Matvalue;
import net.bsoftlab.model.Unitofmsr;
import net.bsoftlab.service.MatvalueService;
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
public class MatvalueServiceImpl implements MatvalueService {

    private static final String GroupNotExistMessageCode = "group.not.exist";
    private static final String MatvalueNotExistMessageCode = "matvalue.not.exist";
    private static final String MatvaluePrimaryKeyInvalidMessageCode = "matvalue.primaryKey.invalid";
    private static final String MatvalueUsedInSalePricesMessageCode = "matvalue.used.in.salePrices";
    private static final String UnitofmsrNotExistMessageCode = "unitofmsr.not.exist";

    private GroupDao groupDao = null;
    private MatvalueDao matvalueDao = null;
    private UnitofmsrDao unitofmsrDao = null;

    @PreDestroy
    public void destroy(){}
    @PostConstruct
    public void init(){}

    @Autowired
    @Qualifier("groupDaoJdbcTemplate")
    public void setGroupDao(
            GroupDao groupDao) {
        this.groupDao = groupDao;
    }
    @Autowired
    @Qualifier("matvalueDaoJdbcTemplate")
    public void setMatvalueDao(
            MatvalueDao matvalueDao) {
        this.matvalueDao = matvalueDao;
    }
    @Autowired
    @Qualifier("unitofmsrDaoJdbcTemplate")
    public void setUnitofmsrDao(
            UnitofmsrDao unitofmsrDao) {
        this.unitofmsrDao = unitofmsrDao;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class, timeout = 10)
    public void deleteMatvalue(String code) throws ServiceException {
        Matvalue matvaluePersistent = this.matvalueDao.getMatvalue(code);
        if(matvaluePersistent == null) {
            throw new ServiceException(MatvalueNotExistMessageCode);
        }
        if(matvaluePersistent.getSalePriceSet() != null &&
                !matvaluePersistent.getSalePriceSet().isEmpty()) {
            throw new ServiceException(MatvalueUsedInSalePricesMessageCode);
        }
        this.matvalueDao.deleteMatvalue(matvaluePersistent);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class, timeout = 10)
    public void insertMatvalue(Matvalue matvalue) throws ServiceException {
        Matvalue matvaluePersistent = this.matvalueDao.getMatvalue(matvalue.getCode());
        if(matvaluePersistent != null) {
            throw new ServiceException(MatvaluePrimaryKeyInvalidMessageCode);
        }

        String unitofmsrCode = matvalue.getUnitofmsr().getCode();
        Unitofmsr unitofmsrPersistent = this.unitofmsrDao.getUnitofmsr(unitofmsrCode);
        if(unitofmsrPersistent == null ||
                !unitofmsrPersistent.equals(matvalue.getUnitofmsr())) {
            throw new ServiceException(UnitofmsrNotExistMessageCode);
        }

        String groupCode = matvalue.getGroup().getCode();
        Group groupPersistent = this.groupDao.getGroup(groupCode);
        if(groupPersistent == null ||
                !groupPersistent.equals(matvalue.getGroup())) {
            throw new ServiceException(GroupNotExistMessageCode);
        }
        this.matvalueDao.insertMatvalue(matvalue);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class, timeout = 10)
    public void updateMatvalue(Matvalue matvalue) throws ServiceException {
        Matvalue matvaluePersistent = this.matvalueDao.getMatvalue(matvalue.getCode());
        if(matvaluePersistent == null) {
            throw new ServiceException(MatvalueNotExistMessageCode);
        }

        String unitofmsrCode = matvalue.getUnitofmsr().getCode();
        Unitofmsr unitofmsrPersistent = this.unitofmsrDao.getUnitofmsr(unitofmsrCode);
        if(unitofmsrPersistent == null ||
                !unitofmsrPersistent.equals(matvalue.getUnitofmsr())) {
            throw new ServiceException(UnitofmsrNotExistMessageCode);
        }

        String groupCode = matvalue.getGroup().getCode();
        Group groupPersistent = this.groupDao.getGroup(groupCode);
        if(groupPersistent == null ||
                !groupPersistent.equals(matvalue.getGroup())) {
            throw new ServiceException(GroupNotExistMessageCode);
        }
        this.matvalueDao.updateMatvalue(matvalue);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public Matvalue getMatvalue(String code) {
        return this.matvalueDao.getMatvalue(code);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public List<Matvalue> getMatvalueList() {
        return this.matvalueDao.getMatvalueList(0, 0);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public List<Matvalue> getMatvalueList(
            String groupCode, String unitofmsrCode, int start, int size)
            throws ServiceException {

        Group groupPersistent = null;
        if (groupCode != null && !groupCode.isEmpty()) {
            groupPersistent = this.groupDao.getGroup(groupCode);
            if(groupPersistent == null) {
                throw new ServiceException(GroupNotExistMessageCode);
            }
        }

        Unitofmsr unitofmsrPersistent = null;
        if (unitofmsrCode != null && !unitofmsrCode.isEmpty()) {
            unitofmsrPersistent = this.unitofmsrDao.getUnitofmsr(unitofmsrCode);
            if(unitofmsrPersistent == null) {
                throw new ServiceException(UnitofmsrNotExistMessageCode);
            }
        }

        if((groupCode != null && !groupCode.isEmpty())
                && (unitofmsrCode == null || unitofmsrCode.isEmpty())
                && (start > 0 && size > 0)) {
            return this.matvalueDao.getMatvalueListGroup(groupPersistent, start, size);
        } else if((groupCode != null && !groupCode.isEmpty())
                && (unitofmsrCode == null || unitofmsrCode.isEmpty())
                && (start == 0 && size == 0)) {
            return this.matvalueDao.getMatvalueListGroup(groupPersistent, start, size);
        } else if((groupCode == null || groupCode.isEmpty())
                && (unitofmsrCode != null && !unitofmsrCode.isEmpty())
                && (start > 0 && size > 0)) {
            return this.matvalueDao.getMatvalueListUnitofmsr(unitofmsrPersistent, start, size);
        } else if((groupCode == null || groupCode.isEmpty())
                && (unitofmsrCode != null && !unitofmsrCode.isEmpty())
                && (start == 0 && size == 0)) {
            return this.matvalueDao.getMatvalueListUnitofmsr(unitofmsrPersistent, start, size);
        } else if((groupCode == null || groupCode.isEmpty())
                && (unitofmsrCode == null || unitofmsrCode.isEmpty())
                && (start > 0 && size > 0)) {
            return this.matvalueDao.getMatvalueList(start, size);
        } else if((groupCode == null || groupCode.isEmpty())
                && (unitofmsrCode == null || unitofmsrCode.isEmpty())
                && (start == 0 && size == 0)) {
            return this.matvalueDao.getMatvalueList(start, size);
        } else {
            return null;
        }
    }
}