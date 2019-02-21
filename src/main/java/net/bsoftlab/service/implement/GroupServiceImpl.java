package net.bsoftlab.service.implement;

import net.bsoftlab.dao.MatvalueDao;
import net.bsoftlab.dao.GroupDao;
import net.bsoftlab.model.Group;
import net.bsoftlab.service.GroupService;
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
public class GroupServiceImpl implements GroupService {

    private static final String GroupNotExistMessageCode = "group.not.exist";
    private static final String GroupUsedInMatvaluesMessageCode = "group.used.in.matvalues";
    private static final String GroupPrimaryKeyInvalidMessageCode = "group.primaryKey.invalid";

    private GroupDao groupDao = null;
    private MatvalueDao matvalueDao = null;

    @PreDestroy
    public void destroy(){}
    @PostConstruct
    public void init(){}

    @Autowired
    @Qualifier("groupDaoJpaTemplate")
    public void setGroupDao(
            GroupDao groupDao) {
        this.groupDao = groupDao;
    }
    @Autowired
    @Qualifier("matvalueDaoJpaTemplate")
    public void setMatvalueDao(
            MatvalueDao matvalueDao) {
        this.matvalueDao = matvalueDao;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class, timeout = 10)
    public void deleteGroup(String code) throws ServiceException {
        Group groupPersistent = this.groupDao.getGroup(code);
        if (groupPersistent == null) {
            throw new ServiceException(GroupNotExistMessageCode);
        }
        if (this.matvalueDao.existMatvalue(groupPersistent)) {
            throw new ServiceException(GroupUsedInMatvaluesMessageCode);
        }
        this.groupDao.deleteGroup(groupPersistent);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class, timeout = 10)
    public void insertGroup(Group group) throws ServiceException {
        Group groupPersistent = this.groupDao.getGroup(group.getCode());
        if(groupPersistent != null) {
            throw new ServiceException(GroupPrimaryKeyInvalidMessageCode);
        }
        this.groupDao.insertGroup(group);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class, timeout = 10)
    public void updateGroup(Group group) throws ServiceException {
        Group groupPersistent = this.groupDao.getGroup(group.getCode());
        if(groupPersistent == null) {
            throw new ServiceException(GroupNotExistMessageCode);
        }
        this.groupDao.updateGroup(group);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public Group getGroup(String code) {
        return this.groupDao.getGroup(code);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public List<Group> getGroupList() {
        return this.groupDao.getGroupList(0, 0);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public List<Group> getGroupList(int start, int size) {
        if(start > 0 && size > 0) {
            return this.groupDao.getGroupList(start, size);
        } else if(start == 0 && size == 0) {
            return this.groupDao.getGroupList(start, size);
        } else {
            return null;
        }
    }
}