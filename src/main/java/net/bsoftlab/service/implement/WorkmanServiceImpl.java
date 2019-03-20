package net.bsoftlab.service.implement;

import net.bsoftlab.dao.WorkmanDao;
import net.bsoftlab.model.Workman;
import net.bsoftlab.service.WorkmanService;
import net.bsoftlab.service.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.util.List;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WorkmanServiceImpl implements WorkmanService {

    private static final String WorkmanNotExistMessageCode = "workman.not.exist";
    private static final String WorkmanUniqueKeyInvalidMessageCode = "workman.uniqueKey.invalid";

    private BCryptPasswordEncoder bCryptPasswordEncoder = null;
    private WorkmanDao workmanDao = null;

    @PreDestroy
    public void destroy(){}
    @PostConstruct
    public void init(){}

    @Autowired
    public void setBCryptPasswordEncoder(
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    @Autowired
    @Qualifier("workmanDaoJdbcTemplate")
    public void setWorkmanDao(
            WorkmanDao workmanDao) {
        this.workmanDao = workmanDao;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class, timeout = 10)
    public void deleteWorkman(Integer ID) throws ServiceException {
        Workman workmanPersistent = this.workmanDao.getWorkman(ID);
        if(workmanPersistent == null) {
            throw new ServiceException(WorkmanNotExistMessageCode);
        }
        this.workmanDao.deleteWorkman(workmanPersistent);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class, timeout = 10)
    public void insertWorkman(Workman workman) throws ServiceException {
        Workman workmanPersistent = this.workmanDao.getWorkman(null, workman.getName());
        if (workmanPersistent != null) {
            throw new ServiceException(WorkmanUniqueKeyInvalidMessageCode);
        }
        workman.setPassword(this.bCryptPasswordEncoder.encode(workman.getPassword()));
        this.workmanDao.insertWorkman(workman);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class, timeout = 10)
    public void updateWorkman(Workman workman) throws ServiceException {

        Workman workmanPersistent = this.workmanDao.getWorkman(workman.getID());
        if(workmanPersistent == null) {
            throw new ServiceException(WorkmanNotExistMessageCode);
        }
        Workman duplicateWorkman = this.workmanDao.getWorkman(workman.getID(), workman.getName());
        if (duplicateWorkman != null) {
            throw new ServiceException(WorkmanUniqueKeyInvalidMessageCode);
        }
        this.workmanDao.updateWorkman(workman);
   }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public Workman getWorkman(Integer ID) {
        return this.workmanDao.getWorkman(ID);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public Workman getWorkman(String name) {
        return this.workmanDao.getWorkman(0, name);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public List<Workman> getWorkmanList() {
        return this.workmanDao.getWorkmanList();
    }
}