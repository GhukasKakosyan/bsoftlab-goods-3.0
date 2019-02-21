package net.bsoftlab.resource.assembler;

import net.bsoftlab.model.Role;
import net.bsoftlab.resource.RoleResource;
import net.bsoftlab.controller.rest.RoleRestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.ConversionService;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RoleResourceAssembler extends
        ResourceAssemblerSupport<Role, RoleResource> {

    private ConversionService conversionService = null;

    public RoleResourceAssembler(
            Class<RoleRestController> roleRestControllerClass,
            Class<RoleResource> roleResourceClass) {
        super(roleRestControllerClass, roleResourceClass);
    }

    public RoleResourceAssembler () {
        this(RoleRestController.class, RoleResource.class);
    }

    @Autowired
    public void setConversionService(
            ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public RoleResource toResource(Role role) {
        if (role == null) {
            return null;
        }
        String code = this.conversionService.convert(role.getID(), String.class);
        RoleResource roleResource = new RoleResource();
        roleResource.setCode(String.valueOf(code));
        roleResource.setName(role.getName());
        roleResource.add(ControllerLinkBuilder.linkTo(
                RoleRestController.class).slash(role.getID()).withSelfRel());
        return roleResource;
    }

    public List<RoleResource> toResources(List<Role> roleList) {
        if (roleList == null) {
            return null;
        }
        return super.toResources(roleList);
    }
}