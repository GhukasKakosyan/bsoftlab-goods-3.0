package net.bsoftlab.resource.converter.entity;

import net.bsoftlab.model.Group;
import net.bsoftlab.model.Matvalue;
import net.bsoftlab.model.Unitofmsr;

import net.bsoftlab.resource.GroupResource;
import net.bsoftlab.resource.MatvalueResource;
import net.bsoftlab.resource.UnitofmsrResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MatvalueConverter implements Converter<MatvalueResource, Matvalue> {

    private GroupConverter groupConverter = null;
    private UnitofmsrConverter unitofmsrConverter = null;

    @Autowired
    public void setGroupConverter(
            GroupConverter groupConverter) {
        this.groupConverter = groupConverter;
    }
    @Autowired
    public void setUnitofmsrConverter(
            UnitofmsrConverter unitofmsrConverter) {
        this.unitofmsrConverter = unitofmsrConverter;
    }

    @Override
    public Matvalue convert(MatvalueResource matvalueResource) {
        if (matvalueResource == null) {
            return null;
        }
        GroupResource groupResource = matvalueResource.getGroupResource();
        Group group = this.groupConverter.convert(groupResource);
        UnitofmsrResource unitofmsrResource = matvalueResource.getUnitofmsrResource();
        Unitofmsr unitofmsr = this.unitofmsrConverter.convert(unitofmsrResource);

        Matvalue matvalue = new Matvalue();
        matvalue.setCode(matvalueResource.getCode());
        matvalue.setName(matvalueResource.getName());
        matvalue.setUnitofmsr(unitofmsr);
        matvalue.setGroup(group);
        return matvalue;
    }
}