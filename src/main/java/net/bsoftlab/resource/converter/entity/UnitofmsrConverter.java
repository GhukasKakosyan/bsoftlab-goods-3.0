package net.bsoftlab.resource.converter.entity;

import net.bsoftlab.model.Unitofmsr;
import net.bsoftlab.resource.UnitofmsrResource;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UnitofmsrConverter implements Converter<UnitofmsrResource, Unitofmsr> {

    @Override
    public Unitofmsr convert(UnitofmsrResource unitofmsrResource) {
        if (unitofmsrResource == null) {
            return null;
        }
        Unitofmsr unitofmsr = new Unitofmsr();
        unitofmsr.setCode(unitofmsrResource.getCode());
        unitofmsr.setShortName(unitofmsrResource.getShortName());
        unitofmsr.setLongName(unitofmsrResource.getLongName());
        return unitofmsr;
    }
}
