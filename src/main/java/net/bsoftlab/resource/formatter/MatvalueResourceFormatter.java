package net.bsoftlab.resource.formatter;

import net.bsoftlab.model.Matvalue;
import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.service.MatvalueService;
import net.bsoftlab.resource.assembler.MatvalueResourceAssembler;

import net.bsoftlab.resource.MatvalueResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MatvalueResourceFormatter implements Formatter<MatvalueResource>{

    private MatvalueResourceAssembler matvalueResourceAssembler;
    private MatvalueService matvalueService;

    @Autowired
    public MatvalueResourceFormatter(
            MatvalueResourceAssembler matvalueResourceAssembler,
            MatvalueService matvalueService) {
        this.matvalueResourceAssembler = matvalueResourceAssembler;
        this.matvalueService = matvalueService;
    }

    @Override
    public MatvalueResource parse(String code, Locale locale) throws ServiceException {
        Matvalue matvalue = this.matvalueService.getMatvalue(code);
        return this.matvalueResourceAssembler.toResource(matvalue);
    }
    @Override
    public String print(MatvalueResource matvalueResource, Locale locale) {
        try {
            return matvalueResource.toString();
        } catch (NullPointerException nullPointerException) {
            return null;
        }
    }
}
