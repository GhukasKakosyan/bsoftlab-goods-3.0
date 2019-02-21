package net.bsoftlab.resource.assembler;

import net.bsoftlab.model.Matvalue;
import net.bsoftlab.controller.rest.MatvalueRestController;
import net.bsoftlab.resource.GroupResource;
import net.bsoftlab.resource.MatvalueResource;
import net.bsoftlab.resource.UnitofmsrResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MatvalueResourceAssembler extends
        ResourceAssemblerSupport<Matvalue, MatvalueResource> {

    private UnitofmsrResourceAssembler unitofmsrResourceAssembler = null;
    private GroupResourceAssembler groupResourceAssembler = null;

    public MatvalueResourceAssembler(
            Class<MatvalueRestController> matvalueRestControllerClass,
            Class<MatvalueResource> matvalueResourceClass) {
        super(matvalueRestControllerClass, matvalueResourceClass);
    }
    public MatvalueResourceAssembler() {
        this(MatvalueRestController.class,
                MatvalueResource.class);
    }

    @Autowired
    public void setUnitofmsrResourceAssembler(
            UnitofmsrResourceAssembler unitofmsrResourceAssembler) {
        this.unitofmsrResourceAssembler = unitofmsrResourceAssembler;
    }
    @Autowired
    public void setGroupResourceAssembler(
            GroupResourceAssembler groupResourceAssembler) {
        this.groupResourceAssembler = groupResourceAssembler;
    }

    @Override
    public MatvalueResource toResource(Matvalue matvalue) {
        if (matvalue == null) {
            return null;
        }
        UnitofmsrResource unitofmsrResource =
                this.unitofmsrResourceAssembler.toResource(matvalue.getUnitofmsr());
        GroupResource groupResource =
                this.groupResourceAssembler.toResource(matvalue.getGroup());
        MatvalueResource matvalueResource = this.instantiateResource(matvalue);
        matvalueResource.setCode(matvalue.getCode());
        matvalueResource.setName(matvalue.getName());
        matvalueResource.setUnitofmsrResource(unitofmsrResource);
        matvalueResource.setGroupResource(groupResource);
        matvalueResource.add(ControllerLinkBuilder.linkTo(
                MatvalueRestController.class).slash(matvalue.getCode()).withSelfRel());
        return matvalueResource;
    }

    public List<MatvalueResource> toResources(List<Matvalue> matvalueList) {
        if (matvalueList == null) {
            return null;
        }
        return super.toResources(matvalueList);
    }
}