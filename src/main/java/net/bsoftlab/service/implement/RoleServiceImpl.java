package net.bsoftlab.service.implement;

import net.bsoftlab.dao.RoleDao;
import net.bsoftlab.model.Role;
import net.bsoftlab.service.RoleService;

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
public class RoleServiceImpl implements RoleService {

    private RoleDao roleDao = null;

    @Autowired
    @Qualifier("roleDaoJpaTemplate")
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public Role getRole(Integer ID) {
        return this.roleDao.getRole(ID);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public List<Role> getRoleList() {
        return this.roleDao.getRoleList();
    }
}