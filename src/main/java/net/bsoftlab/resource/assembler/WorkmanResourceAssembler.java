package net.bsoftlab.resource.assembler;

import net.bsoftlab.model.Role;
import net.bsoftlab.model.Workman;
import net.bsoftlab.resource.RoleResource;
import net.bsoftlab.controller.rest.WorkmanRestController;
import net.bsoftlab.resource.AddressResource;
import net.bsoftlab.resource.WorkmanResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.ConversionService;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WorkmanResourceAssembler extends
        ResourceAssemblerSupport<Workman, WorkmanResource>{

    private ConversionService conversionService = null;
    private AddressResourceAssembler addressResourceAssembler = null;
    private RoleResourceAssembler roleResourceAssembler = null;

    public WorkmanResourceAssembler(
            Class<WorkmanRestController> workmanRestControllerClass,
            Class<WorkmanResource> workmanResourceClass) {
        super(workmanRestControllerClass, workmanResourceClass);
    }
    public WorkmanResourceAssembler() {
        this(WorkmanRestController.class, WorkmanResource.class);
    }

    @Autowired
    public void setConversionService(
            ConversionService conversionService) {
        this.conversionService = conversionService;
    }
    @Autowired
    public void setAddressResourceAssembler(
            AddressResourceAssembler addressResourceAssembler) {
        this.addressResourceAssembler = addressResourceAssembler;
    }
    @Autowired
    public void setRoleResourceAssembler(
            RoleResourceAssembler roleResourceAssembler) {
        this.roleResourceAssembler = roleResourceAssembler;
    }

    @Override
    public WorkmanResource toResource(Workman workman) {
        if (workman == null) {
            return null;
        }
        AddressResource addressResource =
                this.addressResourceAssembler.toResource(workman.getAddress());

        List<Role> roleListWorkman = new ArrayList<>(workman.getRoles());
        RoleResource roleResource = null;
        if (!roleListWorkman.isEmpty()) {
            roleResource = this.roleResourceAssembler.toResource(roleListWorkman.get(0));
        }

        String code = this.conversionService.convert(workman.getID(), String.class);

        WorkmanResource workmanResource = this.instantiateResource(workman);
        workmanResource.setCode(code);
        workmanResource.setName(workman.getName());
        workmanResource.setPassword(workman.getPassword());
        workmanResource.setFirstName(workman.getFirstName());
        workmanResource.setLastName(workman.getLastName());
        workmanResource.setPhones(workman.getPhones());
        workmanResource.setAddressResource(addressResource);
        workmanResource.setRoleResource(roleResource);
        workmanResource.add(ControllerLinkBuilder
                .linkTo(WorkmanRestController.class)
                .slash(workmanResource.getCode()).withSelfRel());
        return workmanResource;
    }

    public List<WorkmanResource> toResources(List<Workman> workmanList) {
        if (workmanList == null) {
            return null;
        }
        return super.toResources(workmanList);
    }
}