package net.bsoftlab.resource.formatter;

import net.bsoftlab.model.Unitofmsr;
import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.service.UnitofmsrService;
import net.bsoftlab.resource.assembler.UnitofmsrResourceAssembler;
import net.bsoftlab.resource.UnitofmsrResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UnitofmsrResourceFormatter implements Formatter<UnitofmsrResource>{

    private UnitofmsrResourceAssembler unitofmsrResourceAssembler;
    private UnitofmsrService unitofmsrService;

    @Autowired
    public UnitofmsrResourceFormatter(
            UnitofmsrResourceAssembler unitofmsrResourceAssembler,
            UnitofmsrService unitofmsrService) {
        this.unitofmsrResourceAssembler = unitofmsrResourceAssembler;
        this.unitofmsrService = unitofmsrService;
    }

    @Override
    public UnitofmsrResource parse(String code, Locale locale) throws ServiceException {
        Unitofmsr unitofmsr = this.unitofmsrService.getUnitofmsr(code);
        return this.unitofmsrResourceAssembler.toResource(unitofmsr);
    }
    @Override
    public String print(UnitofmsrResource unitofmsrResource, Locale locale) {
        try {
            return unitofmsrResource.toString();
        } catch (NullPointerException nullPointerException) {
            return null;
        }
    }
}
