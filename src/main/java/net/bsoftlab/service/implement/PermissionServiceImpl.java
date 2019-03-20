package net.bsoftlab.service.implement;

import net.bsoftlab.dao.PermissionDao;
import net.bsoftlab.model.Permission;
import net.bsoftlab.service.PermissionService;
import net.bsoftlab.service.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PermissionServiceImpl implements PermissionService {

    private PermissionDao permissionDao = null;

    @Autowired
    @Qualifier("permissionDaoJdbcTemplate")
    public void setPermissionDao(
            PermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public Permission getPermission(Integer ID) {
        return this.permissionDao.getPermission(ID);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public Permission getPermission(String name) {
        return this.permissionDao.getPermission(name);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public List<Permission> getPermissionList() {
        return this.permissionDao.getPermissionList();
    }
}
