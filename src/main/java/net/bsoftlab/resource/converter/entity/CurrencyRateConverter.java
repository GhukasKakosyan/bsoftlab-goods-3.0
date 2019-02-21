package net.bsoftlab.resource.converter.entity;

import net.bsoftlab.model.Currency;
import net.bsoftlab.model.CurrencyRate;

import net.bsoftlab.resource.CurrencyRateResource;
import net.bsoftlab.resource.CurrencyResource;
import net.bsoftlab.resource.converter.type.StringToDateConverter;
import net.bsoftlab.resource.converter.type.StringToDoubleConverter;
import net.bsoftlab.resource.converter.type.StringToIntegerConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.lang.Double;
import java.lang.Integer;
import java.lang.String;
import java.util.Date;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CurrencyRateConverter implements
        Converter<CurrencyRateResource, CurrencyRate> {

    private CurrencyConverter currencyConverter = null;
    private StringToDateConverter stringToDateConverter = null;
    private StringToDoubleConverter stringToDoubleConverter = null;
    private StringToIntegerConverter stringToIntegerConverter = null;

    @Autowired
    public void setCurrencyConverter(
            CurrencyConverter currencyConverter) {
        this.currencyConverter = currencyConverter;
    }
    @Autowired
    public void setStringToDateConverter(
            StringToDateConverter stringToDateConverter) {
        this.stringToDateConverter = stringToDateConverter;
    }
    @Autowired
    public void setStringToDoubleConverter(
            StringToDoubleConverter stringToDoubleConverter) {
        this.stringToDoubleConverter = stringToDoubleConverter;
    }
    @Autowired
    public void setStringToIntegerConverter(
            StringToIntegerConverter stringToIntegerConverter) {
        this.stringToIntegerConverter = stringToIntegerConverter;
    }

    @Override
    public CurrencyRate convert(CurrencyRateResource currencyRateResource) {
        if (currencyRateResource == null) {
            return null;
        }

        Integer ID;
        String code = currencyRateResource.getCode();
        if (code == null || code.trim().isEmpty()) {
            ID = null;
        } else {
            ID = this.stringToIntegerConverter.convert(code);
        }

        String dateText = currencyRateResource.getDate();
        Date date = this.stringToDateConverter.convert(dateText);

        String rateText = currencyRateResource.getRate();
        Double rate = this.stringToDoubleConverter.convert(rateText);

        String quantityText = currencyRateResource.getQuantity();
        Double quantity = this.stringToDoubleConverter.convert(quantityText);

        CurrencyResource currencyResource = currencyRateResource.getCurrencyResource();
        Currency currency = this.currencyConverter.convert(currencyResource);

        CurrencyRate currencyRate = new CurrencyRate();
        currencyRate.setID(ID);
        currencyRate.setCurrency(currency);
        currencyRate.setDate(date);
        currencyRate.setRate(rate);
        currencyRate.setQuantity(quantity);
        return currencyRate;
    }
}