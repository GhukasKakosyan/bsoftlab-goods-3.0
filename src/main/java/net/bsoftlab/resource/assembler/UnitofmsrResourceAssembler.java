package net.bsoftlab.resource.assembler;

import net.bsoftlab.model.Unitofmsr;
import net.bsoftlab.controller.rest.UnitofmsrRestController;
import net.bsoftlab.resource.UnitofmsrResource;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UnitofmsrResourceAssembler extends
        ResourceAssemblerSupport<Unitofmsr, UnitofmsrResource> {

    public UnitofmsrResourceAssembler(
            Class<UnitofmsrRestController> unitofmsrRestControllerClass,
            Class<UnitofmsrResource> unitofmsrResourceClass) {
        super(unitofmsrRestControllerClass, unitofmsrResourceClass);
    }
    public UnitofmsrResourceAssembler() {
        this(UnitofmsrRestController.class, UnitofmsrResource.class);
    }

    @Override
    public UnitofmsrResource toResource(Unitofmsr unitofmsr) {
        if (unitofmsr == null) {
            return null;
        }
        UnitofmsrResource unitofmsrResource = this.instantiateResource(unitofmsr);
        unitofmsrResource.setCode(unitofmsr.getCode());
        unitofmsrResource.setShortName(unitofmsr.getShortName());
        unitofmsrResource.setLongName(unitofmsr.getLongName());
        unitofmsrResource.add(ControllerLinkBuilder
                .linkTo(UnitofmsrRestController.class)
                .slash(unitofmsr.getCode()).withSelfRel());
        return unitofmsrResource;
    }

    public List<UnitofmsrResource> toResources(List<Unitofmsr> unitofmsrList) {
        if (unitofmsrList == null) {
            return null;
        }
        return super.toResources(unitofmsrList);
    }
}