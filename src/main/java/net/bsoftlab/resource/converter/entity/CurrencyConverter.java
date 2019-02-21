package net.bsoftlab.resource.converter.entity;

import net.bsoftlab.model.Currency;
import net.bsoftlab.resource.CurrencyResource;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CurrencyConverter implements Converter<CurrencyResource, Currency> {

    @Override
    public Currency convert(CurrencyResource currencyResource) {
        if (currencyResource == null) {
            return null;
        }
        Currency currency = new Currency();
        currency.setCode(currencyResource.getCode());
        currency.setShortName(currencyResource.getShortName());
        currency.setLongName(currencyResource.getLongName());
        currency.setCountry(currencyResource.getCountry());
        currency.setAdditionalInformation(currencyResource.getAdditionalInformation());
        return currency;
    }
}