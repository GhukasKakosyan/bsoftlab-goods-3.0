package net.bsoftlab.resource.converter.entity;

import net.bsoftlab.model.Address;
import net.bsoftlab.model.Department;
import net.bsoftlab.resource.AddressResource;
import net.bsoftlab.resource.DepartmentResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DepartmentConverter implements Converter<DepartmentResource, Department> {

    private AddressConverter addressConverter = null;

    @Autowired
    public void setAddressConverter(
            AddressConverter addressConverter) {
        this.addressConverter = addressConverter;
    }

    @Override
    public Department convert(DepartmentResource departmentResource) {
        if (departmentResource == null) {
            return null;
        }
        AddressResource addressResource = departmentResource.getAddressResource();
        Address address = this.addressConverter.convert(addressResource);

        Department department = new Department();
        department.setCode(departmentResource.getCode());
        department.setName(departmentResource.getName());
        department.setAddress(address);
        department.setPhones(departmentResource.getPhones());
        department.setFaxes(departmentResource.getFaxes());
        department.setWebSite(departmentResource.getWebSite());
        department.setEmailAddress(departmentResource.getEmailAddress());
        department.setAdditionalInformation(departmentResource.getAdditionalInformation());
        return department;
    }
}