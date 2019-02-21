package net.bsoftlab.resource.validator;

import net.bsoftlab.resource.CurrencyResource;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CurrencyResourceValidator implements Validator {

    private static final String CurrencyEmptyMessageCode = "currency.empty";
    private static final String CurrencyInvalidMessageCode = "currency.invalid";
    private static final String CurrencyCodeEmptyMessageCode = "currency.code.empty";
    private static final String CurrencyCodeSizeInvalidMessageCode = "currency.code.size.invalid";
    private static final String CurrencyShortNameEmptyMessageCode = "currency.shortName.empty";
    private static final String CurrencyShortNameSizeInvalidMessageCode = "currency.shortName.size.invalid";
    private static final String CurrencyLongNameEmptyMessageCode = "currency.longName.empty";
    private static final String CurrencyLongNameSizeInvalidMessageCode = "currency.longName.size.invalid";
    private static final String CurrencyCountryEmptyMessageCode = "currency.country.empty";
    private static final String CurrencyCountrySizeInvalidMessageCode = "currency.country.size.invalid";
    private static final String CurrencyAdditionalInformationSizeInvalidMessageCode = "currency.additionalInformation.size.invalid";

    @Override
    public boolean supports(Class<?> clazz) {
        return CurrencyResource.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object resource, Errors errors) {
        if (resource == null) {
            errors.reject(CurrencyEmptyMessageCode);
            return;
        }
        CurrencyResource currencyResource;
        try {
            currencyResource = (CurrencyResource) resource;
        } catch (ClassCastException classCastException) {
            errors.reject(CurrencyInvalidMessageCode);
            return;
        }

        if (currencyResource.getCode() == null ||
                currencyResource.getCode().trim().isEmpty()) {
            errors.rejectValue("code", CurrencyCodeEmptyMessageCode);
        } else if (currencyResource.getCode().trim().length() != 3) {
            errors.rejectValue("code", CurrencyCodeSizeInvalidMessageCode);
        } else {
            currencyResource.setCode(currencyResource.getCode().trim());
        }

        if (currencyResource.getShortName() == null ||
                currencyResource.getShortName().trim().isEmpty()) {
            errors.rejectValue("shortName", CurrencyShortNameEmptyMessageCode);
        } else if (currencyResource.getShortName().trim().length() > 10) {
            errors.rejectValue("shortName", CurrencyShortNameSizeInvalidMessageCode);
        } else {
            currencyResource.setShortName(currencyResource.getShortName().trim());
        }

        if (currencyResource.getLongName() == null ||
                currencyResource.getLongName().trim().isEmpty()) {
            errors.rejectValue("longName", CurrencyLongNameEmptyMessageCode);
        } else if (currencyResource.getLongName().trim().length() > 100) {
            errors.rejectValue("longName", CurrencyLongNameSizeInvalidMessageCode);
        } else {
            currencyResource.setLongName(currencyResource.getLongName().trim());
        }

        if (currencyResource.getCountry() == null ||
                currencyResource.getCountry().trim().isEmpty()) {
            errors.rejectValue("country", CurrencyCountryEmptyMessageCode);
        } else if (currencyResource.getCountry().trim().length() > 100) {
            errors.rejectValue("country", CurrencyCountrySizeInvalidMessageCode);
        } else {
            currencyResource.setCountry(currencyResource.getCountry().trim());
        }

        if (currencyResource.getAdditionalInformation() != null &&
                currencyResource.getAdditionalInformation().trim().length() > 500) {
            errors.rejectValue("additionalInformation",
                    CurrencyAdditionalInformationSizeInvalidMessageCode);
        }
    }
}