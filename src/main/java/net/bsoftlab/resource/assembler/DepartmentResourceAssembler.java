package net.bsoftlab.resource.assembler;

import net.bsoftlab.model.Department;
import net.bsoftlab.controller.rest.DepartmentRestController;
import net.bsoftlab.resource.AddressResource;
import net.bsoftlab.resource.DepartmentResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DepartmentResourceAssembler extends
        ResourceAssemblerSupport<Department, DepartmentResource> {

    private AddressResourceAssembler addressResourceAssembler = null;

    public DepartmentResourceAssembler(
            Class<DepartmentRestController> departmentRestControllerClass,
            Class<DepartmentResource> departmentResourceClass) {
        super(departmentRestControllerClass, departmentResourceClass);
    }
    public DepartmentResourceAssembler() {
        this(DepartmentRestController.class,
                DepartmentResource.class);
    }

    @Autowired
    public void setAddressResourceAssembler(
            AddressResourceAssembler addressResourceAssembler) {
        this.addressResourceAssembler = addressResourceAssembler;
    }

    @Override
    public DepartmentResource toResource(Department department) {
        if (department == null) {
            return null;
        }
        AddressResource addressResource =
                this.addressResourceAssembler.toResource(department.getAddress());
        DepartmentResource departmentResource = this.instantiateResource(department);
        departmentResource.setCode(department.getCode());
        departmentResource.setName(department.getName());
        departmentResource.setAddressResource(addressResource);
        departmentResource.setPhones(department.getPhones());
        departmentResource.setFaxes(department.getFaxes());
        departmentResource.setWebSite(department.getWebSite());
        departmentResource.setEmailAddress(department.getEmailAddress());
        departmentResource.setAdditionalInformation(department.getAdditionalInformation());
        departmentResource.add(ControllerLinkBuilder.linkTo(
                DepartmentRestController.class).slash(department.getCode()).withSelfRel());
        return departmentResource;
    }

    public List<DepartmentResource> toResources(List<Department> departmentList) {
        if (departmentList == null) {
            return null;
        }
        return super.toResources(departmentList);
    }
}