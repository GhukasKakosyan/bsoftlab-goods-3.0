package net.bsoftlab.resource.converter.entity;

import net.bsoftlab.model.Role;
import net.bsoftlab.resource.RoleResource;
import net.bsoftlab.resource.converter.type.StringToIntegerConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RoleConverter implements Converter<RoleResource, Role> {

    private StringToIntegerConverter stringToIntegerConverter = null;

    @Autowired
    public void setStringToIntegerConverter(
            StringToIntegerConverter stringToIntegerConverter) {
        this.stringToIntegerConverter = stringToIntegerConverter;
    }

    @Override
    public Role convert(RoleResource roleResource) {
        if (roleResource == null) {
            return null;
        }
        Integer ID;
        String code = roleResource.getCode();
        if (code == null || code.trim().isEmpty()) {
            ID = null;
        } else {
            ID = this.stringToIntegerConverter.convert(code);
        }
        Role role = new Role();
        role.setID(ID);
        role.setName(roleResource.getName());
        return role;
    }
}
