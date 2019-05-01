package net.bsoftlab.service.implement;

import net.bsoftlab.dao.api.MatvalueDao;
import net.bsoftlab.dao.api.UnitofmsrDao;
import net.bsoftlab.model.Unitofmsr;
import net.bsoftlab.service.UnitofmsrService;
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
public class UnitofmsrServiceImpl implements UnitofmsrService {

    private static final String UnitofmsrNotExistMessageCode = "unitofmsr.not.exist";
    private static final String UnitofmsrPrimaryKeyInvalidMessageCode = "unitofmsr.primaryKey.invalid";
    private static final String UnitofmsrUsedInMatvaluesMessageCode = "unitofmsr.used.in.matvalues";

    private MatvalueDao matvalueDao = null;
    private UnitofmsrDao unitofmsrDao = null;

    @PreDestroy
    public void destroy(){}
    @PostConstruct
    public void init(){}

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
    public void deleteUnitofmsr(String code) throws ServiceException {

        Unitofmsr unitofmsrPersistent = this.unitofmsrDao.getUnitofmsr(code);
        if (unitofmsrPersistent == null) {
            throw new ServiceException(UnitofmsrNotExistMessageCode);
        }
        if (this.matvalueDao.existMatvalue(unitofmsrPersistent)) {
            throw new ServiceException(UnitofmsrUsedInMatvaluesMessageCode);
        }
        this.unitofmsrDao.deleteUnitofmsr(unitofmsrPersistent);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class, timeout = 10)
    public void insertUnitofmsr(Unitofmsr unitofmsr) throws ServiceException {
        Unitofmsr unitofmsrPersistent = this.unitofmsrDao.getUnitofmsr(unitofmsr.getCode());
        if(unitofmsrPersistent != null) {
            throw new ServiceException(UnitofmsrPrimaryKeyInvalidMessageCode);
        }
        this.unitofmsrDao.insertUnitofmsr(unitofmsr);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class, timeout = 10)
    public void updateUnitofmsr(Unitofmsr unitofmsr) throws ServiceException {
        Unitofmsr unitofmsrPersistent = this.unitofmsrDao.getUnitofmsr(unitofmsr.getCode());
        if(unitofmsrPersistent == null) {
            throw new ServiceException(UnitofmsrNotExistMessageCode);
        }
        this.unitofmsrDao.updateUnitofmsr(unitofmsr);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public Unitofmsr getUnitofmsr(String code) {
        return unitofmsrDao.getUnitofmsr(code);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public List<Unitofmsr> getUnitofmsrList() {
        return this.unitofmsrDao.getUnitofmsrList(0, 0);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public List<Unitofmsr> getUnitofmsrList(int start, int size) {
        if(start > 0 && size > 0) {
            return this.unitofmsrDao.getUnitofmsrList(start, size);
        } else if(start == 0 && size == 0) {
            return this.unitofmsrDao.getUnitofmsrList(start, size);
        } else {
            return null;
        }
    }
}