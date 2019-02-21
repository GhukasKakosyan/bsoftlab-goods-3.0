package net.bsoftlab.security;

import net.bsoftlab.model.Permission;
import net.bsoftlab.model.Role;
import net.bsoftlab.model.Workman;
import net.bsoftlab.service.WorkmanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.util.HashSet;
import java.util.Set;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UserDetailsServiceImpl implements UserDetailsService {

    private WorkmanService workmanService = null;

    @PreDestroy
    public void destroy(){}
    @PostConstruct
    public void init(){}

    @Autowired
    public void setWorkmanService(
            WorkmanService workmanServiceImpl) {
        this.workmanService = workmanServiceImpl;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Workman workman = this.workmanService.getWorkman(username);
        if (workman == null) return null;
        Set<Role> roles = workman.getRoles();
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : roles) {
            if (role.getPermissions() != null && !role.getPermissions().isEmpty()) {
                for (Permission permission : role.getPermissions()) {
                    boolean permissionAdded = false;
                    for (GrantedAuthority grantedAuthorityItem : grantedAuthorities) {
                        if (permission.getName().equals(grantedAuthorityItem.getAuthority())) {
                            permissionAdded = true;
                            break;
                        }
                    }
                    if (!permissionAdded) {
                        GrantedAuthority grantedAuthority =
                                new SimpleGrantedAuthority(permission.getName());
                        grantedAuthorities.add(grantedAuthority);
                    }
                }
            }
        }
        return new User(workman.getName(), workman.getPassword(), grantedAuthorities);
    }
}