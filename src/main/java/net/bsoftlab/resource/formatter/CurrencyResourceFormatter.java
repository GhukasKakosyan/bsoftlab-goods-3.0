package net.bsoftlab.resource.formatter;

import net.bsoftlab.model.Currency;
import net.bsoftlab.service.exception.ServiceException;
import net.bsoftlab.service.CurrencyService;
import net.bsoftlab.resource.assembler.CurrencyResourceAssembler;
import net.bsoftlab.resource.CurrencyResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CurrencyResourceFormatter implements Formatter<CurrencyResource> {

    private CurrencyResourceAssembler currencyResourceAssembler;
    private CurrencyService currencyService;

    @Autowired
    public CurrencyResourceFormatter(
            CurrencyResourceAssembler currencyResourceAssembler,
            CurrencyService currencyService) {
        this.currencyResourceAssembler = currencyResourceAssembler;
        this.currencyService = currencyService;
    }

    @Override
    public CurrencyResource parse(String code, Locale locale) throws ServiceException {
        Currency currency = this.currencyService.getCurrency(code);
        return this.currencyResourceAssembler.toResource(currency);
    }
    @Override
    public String print(CurrencyResource currencyResource, Locale locale) {
        try {
            return currencyResource.toString();
        } catch (NullPointerException nullPointerException) {
            return null;
        }
    }
}
