package net.bsoftlab.resource.converter.entity;

import net.bsoftlab.model.Address;
import net.bsoftlab.model.Role;
import net.bsoftlab.model.Workman;

import net.bsoftlab.resource.AddressResource;
import net.bsoftlab.resource.RoleResource;
import net.bsoftlab.resource.WorkmanResource;

import net.bsoftlab.resource.converter.type.StringToIntegerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WorkmanConverter implements Converter<WorkmanResource, Workman> {

    private AddressConverter addressConverter = null;
    private RoleConverter roleConverter = null;
    private StringToIntegerConverter stringToIntegerConverter = null;

    @Autowired
    public void setAddressConverter(
            AddressConverter addressConverter) {
        this.addressConverter = addressConverter;
    }
    @Autowired
    public void setRoleConverter(
            RoleConverter roleConverter) {
        this.roleConverter = roleConverter;
    }
    @Autowired
    public void setStringToIntegerConverter(
            StringToIntegerConverter stringToIntegerConverter) {
        this.stringToIntegerConverter = stringToIntegerConverter;
    }

    @Override
    public Workman convert(WorkmanResource workmanResource) {
        if (workmanResource == null) {
            return null;
        }
        Integer ID;
        String code = workmanResource.getCode();
        if (code == null || code.trim().isEmpty()) {
            ID = null;
        } else {
            ID = this.stringToIntegerConverter.convert(code);
        }

        AddressResource addressResource = workmanResource.getAddressResource();
        Address address = this.addressConverter.convert(addressResource);

        RoleResource roleResource = workmanResource.getRoleResource();
        Role role = this.roleConverter.convert(roleResource);

        Workman workman = new Workman();
        workman.setID(ID);
        workman.setName(workmanResource.getName());
        workman.setPassword(workmanResource.getPassword());
        workman.setFirstName(workmanResource.getFirstName());
        workman.setLastName(workmanResource.getLastName());
        workman.setPhones(workmanResource.getPhones());
        workman.setAddress(address);
        workman.getRoles().add(role);
        return workman;
    }
}