package net.bsoftlab.resource.validator;

import net.bsoftlab.resource.CurrencyRateResource;
import net.bsoftlab.resource.CurrencyResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.lang.IllegalArgumentException;
import java.util.Date;
import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CurrencyRateResourceValidator implements Validator {

    private static final String CurrencyRateEmptyMessageCode = "currencyRate.empty";
    private static final String CurrencyRateInvalidMessageCode = "currencyRate.invalid";
    private static final String CurrencyRateIdNegativeMessageCode = "currencyRate.id.negative";
    private static final String CurrencyRateIdInvalidMessageCode = "currencyRate.id.invalid";
    private static final String CurrencyRateDateEmptyMessageCode = "currencyRate.date.empty";
    private static final String CurrencyRateDateInvalidMessageCode = "currencyRate.date.invalid";
    private static final String CurrencyRateRateEmptyMessageCode = "currencyRate.rate.empty";
    private static final String CurrencyRateRateNegativeMessageCode = "currencyRate.rate.negative";
    private static final String CurrencyRateRateEqualsZeroMessageCode = "currencyRate.rate.equals.zero";
    private static final String CurrencyRateRateInvalidMessageCode = "currencyRate.rate.invalid";
    private static final String CurrencyRateQuantityEmptyMessageCode = "currencyRate.quantity.empty";
    private static final String CurrencyRateQuantityNegativeMessageCode = "currencyRate.quantity.negative";
    private static final String CurrencyRateQuantityEqualsZeroMessageCode = "currencyRate.quantity.equals.zero";
    private static final String CurrencyRateQuantityInvalidMessageCode = "currencyRate.quantity.invalid";

    private ConversionService conversionService = null;
    private CurrencyResourceValidator currencyResourceValidator = null;

    @Autowired
    public void setConversionService(
            ConversionService conversionService) {
        this.conversionService = conversionService;
    }
    @Autowired
    public void setCurrencyResourceValidator(
            CurrencyResourceValidator currencyResourceValidator) {
        this.currencyResourceValidator = currencyResourceValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CurrencyRateResource.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object resource, Errors errors) {
        if (resource == null) {
            errors.reject(CurrencyRateEmptyMessageCode);
            return;
        }
        CurrencyRateResource currencyRateResource;
        try {
            currencyRateResource = (CurrencyRateResource)resource;
        } catch (ClassCastException classCastException) {
            errors.reject(CurrencyRateInvalidMessageCode);
            return;
        }

        String idText = currencyRateResource.getCode();
        if (idText != null && !idText.trim().isEmpty()) {
            try {
                if (this.conversionService.convert(idText, Integer.class) < 0) {
                    errors.rejectValue("code", CurrencyRateIdNegativeMessageCode);
                } else {
                    currencyRateResource.setCode(currencyRateResource.getCode().trim());
                }
            } catch (IllegalArgumentException illegalArgumentException) {
                errors.rejectValue("code", CurrencyRateIdInvalidMessageCode);
            }
        }

        CurrencyResource currencyResource = currencyRateResource.getCurrencyResource();
        BeanPropertyBindingResult beanPropertyBindingResultCurrencyResource =
                new BeanPropertyBindingResult(currencyResource, "currencyResource");
        ValidationUtils.invokeValidator(this.currencyResourceValidator,
                currencyResource, beanPropertyBindingResultCurrencyResource);
        List<ObjectError> objectErrorListCurrencyResource =
                beanPropertyBindingResultCurrencyResource.getAllErrors();
        if (objectErrorListCurrencyResource != null &&
                !objectErrorListCurrencyResource.isEmpty()) {
            for (ObjectError objectError : objectErrorListCurrencyResource) {
                errors.rejectValue("currencyResource", objectError.getCode());
            }
        }

        if (currencyRateResource.getDate() == null ||
                currencyRateResource.getDate().trim().isEmpty()) {
            errors.rejectValue("date", CurrencyRateDateEmptyMessageCode);
        } else {
            try {
                String dateText = currencyRateResource.getDate().trim();
                this.conversionService.convert(dateText, Date.class);
                currencyRateResource.setDate(dateText);
            } catch (IllegalArgumentException illegalArgumentException) {
                errors.rejectValue("date", CurrencyRateDateInvalidMessageCode);
            }
        }

        if (currencyRateResource.getRate() == null ||
                currencyRateResource.getRate().trim().isEmpty()) {
            errors.rejectValue("rate", CurrencyRateRateEmptyMessageCode);
        } else {
            try {
                String rateText = currencyRateResource.getRate().trim();
                Double rate = this.conversionService.convert(rateText, Double.class);
                if (rate < 0.00) {
                    errors.rejectValue("rate", CurrencyRateRateNegativeMessageCode);
                } else if (rate == 0.00) {
                    errors.rejectValue("rate", CurrencyRateRateEqualsZeroMessageCode);
                } else {
                    currencyRateResource.setRate(rateText);
                }
            } catch (IllegalArgumentException illegalArgumentException) {
                errors.rejectValue("rate", CurrencyRateRateInvalidMessageCode);
            }
        }

        if (currencyRateResource.getQuantity() == null ||
                currencyRateResource.getQuantity().trim().isEmpty()) {
            errors.rejectValue("quantity", CurrencyRateQuantityEmptyMessageCode);
        } else {
            try {
                String quantityText = currencyRateResource.getQuantity().trim();
                Double quantity = this.conversionService.convert(quantityText, Double.class);
                if (quantity < 0.000) {
                    errors.rejectValue("quantity", CurrencyRateQuantityNegativeMessageCode);
                } else if (quantity == 0.000) {
                    errors.rejectValue("quantity", CurrencyRateQuantityEqualsZeroMessageCode);
                } else {
                    currencyRateResource.setQuantity(quantityText);
                }
            } catch (IllegalArgumentException illegalArgumentException) {
                errors.rejectValue("quantity", CurrencyRateQuantityInvalidMessageCode);
            }
        }
    }
}