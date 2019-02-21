package net.bsoftlab.resource.assembler;

import net.bsoftlab.model.Permission;
import net.bsoftlab.resource.PermissionResource;
import net.bsoftlab.controller.rest.PermissionRestController;

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
public class PermissionResourceAssembler extends
        ResourceAssemblerSupport<Permission, PermissionResource> {

    private ConversionService conversionService = null;

    public PermissionResourceAssembler (
            Class<PermissionRestController> permissionRestControllerClass,
            Class<PermissionResource> permissionResourceClass){
        super(permissionRestControllerClass, permissionResourceClass);
    }

    public PermissionResourceAssembler(){
        this(PermissionRestController.class,
                PermissionResource.class);
    }

    @Autowired
    public void setConversionService(
            ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public PermissionResource toResource(Permission permission) {
        if (permission == null) {
            return null;
        }
        String code = this.conversionService.convert(permission.getID(), String.class);
        PermissionResource permissionResource = new PermissionResource();
        permissionResource.setCode(code);
        permissionResource.setName(permission.getName());
        permissionResource.add(ControllerLinkBuilder.linkTo(
                PermissionRestController.class).slash(permission.getID()).withSelfRel());
        return permissionResource;
    }

    public List<PermissionResource> toResources(List<Permission> permissionList) {
        if (permissionList == null) {
            return null;
        }
        return super.toResources(permissionList);
    }
}
