package net.bsoftlab.resource.formatter;

import net.bsoftlab.model.Role;
import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.service.RoleService;
import net.bsoftlab.resource.assembler.RoleResourceAssembler;
import net.bsoftlab.resource.RoleResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RoleResourceFormatter implements Formatter<RoleResource>{

    private ConversionService conversionService = null;

    private RoleResourceAssembler roleResourceAssembler;
    private RoleService roleService;

    @Autowired
    public RoleResourceFormatter(
            RoleResourceAssembler roleResourceAssembler,
            RoleService roleService) {
        this.roleResourceAssembler = roleResourceAssembler;
        this.roleService = roleService;
    }

    @Autowired
    public void setConversionService(
            ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public RoleResource parse(String code, Locale locale)
            throws ParseException, ServiceException {
        int ID;
        try {
            ID = this.conversionService.convert(code, Integer.class);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new ParseException("role.id.invalid", 1);
        }
        Role role = this.roleService.getRole(ID);
        return this.roleResourceAssembler.toResource(role);
    }

    @Override
    public String print(RoleResource roleResource, Locale locale) {
        try {
            return roleResource.toString();
        } catch (NullPointerException nullPointerException) {
            return null;
        }
    }
}
