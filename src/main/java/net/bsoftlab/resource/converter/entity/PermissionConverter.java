package net.bsoftlab.resource.converter.entity;

import net.bsoftlab.model.Permission;
import net.bsoftlab.resource.PermissionResource;
import net.bsoftlab.resource.converter.type.StringToIntegerConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PermissionConverter implements Converter<PermissionResource, Permission> {

    private StringToIntegerConverter stringToIntegerConverter = null;

    @Autowired
    public void setStringToIntegerConverter(
            StringToIntegerConverter stringToIntegerConverter) {
        this.stringToIntegerConverter = stringToIntegerConverter;
    }

    @Override
    public Permission convert(PermissionResource permissionResource) {
        if (permissionResource == null) {
            return null;
        }
        Integer ID;
        String code = permissionResource.getCode();
        if (code == null || code.trim().isEmpty()) {
            ID = null;
        } else {
            ID = this.stringToIntegerConverter.convert(code);
        }
        Permission permission = new Permission();
        permission.setID(ID);
        permission.setName(permissionResource.getName());
        return permission;
    }
}