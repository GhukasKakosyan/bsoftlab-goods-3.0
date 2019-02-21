package net.bsoftlab.resource.validator;

import net.bsoftlab.resource.CurrencyResource;
import net.bsoftlab.resource.DepartmentResource;
import net.bsoftlab.resource.MatvalueResource;
import net.bsoftlab.resource.SalePriceResource;

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
public class SalePriceResourceValidator implements Validator {

    private static final String SalePriceEmptyMessageCode = "salePrice.empty";
    private static final String SalePriceInvalidMessageCode = "salePrice.invalid";
    private static final String SalePriceIdNegativeMessageCode = "salePrice.id.negative";
    private static final String SalePriceIdInvalidMessageCode = "salePrice.id.invalid";
    private static final String SalePriceDateEmptyMessageCode = "salePrice.date.empty";
    private static final String SalePriceDateInvalidMessageCode = "salePrice.date.invalid";
    private static final String SalePricePriceEmptyMessageCode = "salePrice.price.empty";
    private static final String SalePricePriceNegativeMessageCode = "salePrice.price.negative";
    private static final String SalePricePriceEqualsZeroMessageCode = "salePrice.price.equals.zero";
    private static final String SalePricePriceInvalidMessageCode = "salePrice.price.invalid";
    private static final String SalePriceQuantityEmptyMessageCode = "salePrice.quantity.empty";
    private static final String SalePriceQuantityNegativeMessageCode = "salePrice.quantity.negative";
    private static final String SalePriceQuantityEqualsZeroMessageCode = "salePrice.quantity.equals.zero";
    private static final String SalePriceQuantityInvalidMessageCode = "salePrice.quantity.invalid";

    private ConversionService conversionService = null;
    private CurrencyResourceValidator currencyResourceValidator = null;
    private DepartmentResourceValidator departmentResourceValidator = null;
    private MatvalueResourceValidator matvalueResourceValidator = null;

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
    @Autowired
    public void setDepartmentResourceValidator(
            DepartmentResourceValidator departmentResourceValidator) {
        this.departmentResourceValidator = departmentResourceValidator;
    }
    @Autowired
    public void setMatvalueResourceValidator(
            MatvalueResourceValidator matvalueResourceValidator) {
        this.matvalueResourceValidator = matvalueResourceValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return SalePriceResource.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object resource, Errors errors) {
        if(resource == null) {
            errors.reject(SalePriceEmptyMessageCode);
            return;
        }
        SalePriceResource salePriceResource;
        try {
            salePriceResource = (SalePriceResource) resource;
        } catch (ClassCastException classCastException) {
            errors.reject(SalePriceInvalidMessageCode);
            return;
        }

        String idText = salePriceResource.getCode();
        if (idText != null && !idText.trim().isEmpty()) {
            try {
                if (this.conversionService.convert(idText, Integer.class) < 0) {
                    errors.rejectValue("code", SalePriceIdNegativeMessageCode);
                } else {
                    salePriceResource.setCode(salePriceResource.getCode().trim());
                }
            } catch (IllegalArgumentException illegalArgumentException) {
                errors.rejectValue("code", SalePriceIdInvalidMessageCode);
            }
        }

        MatvalueResource matvalueResource = salePriceResource.getMatvalueResource();
        BeanPropertyBindingResult beanPropertyBindingResultMatvalueResource =
                new BeanPropertyBindingResult(matvalueResource, "matvalueResource");
        ValidationUtils.invokeValidator(this.matvalueResourceValidator,
                matvalueResource, beanPropertyBindingResultMatvalueResource);
        List<ObjectError> objectErrorListMatvalueResource =
                beanPropertyBindingResultMatvalueResource.getAllErrors();
        if (objectErrorListMatvalueResource != null &&
                !objectErrorListMatvalueResource.isEmpty()) {
            for (ObjectError objectError : objectErrorListMatvalueResource) {
                errors.rejectValue("matvalueResource", objectError.getCode());
            }
        }

        DepartmentResource departmentResource = salePriceResource.getDepartmentResource();
        BeanPropertyBindingResult beanPropertyBindingResultDepartmentResource =
                new BeanPropertyBindingResult(departmentResource, "departmentResource");
        ValidationUtils.invokeValidator(this.departmentResourceValidator,
                departmentResource, beanPropertyBindingResultDepartmentResource);
        List<ObjectError> objectErrorListDepartmentResource =
                beanPropertyBindingResultDepartmentResource.getAllErrors();
        if (objectErrorListDepartmentResource != null &&
                !objectErrorListDepartmentResource.isEmpty()) {
            for (ObjectError objectError : objectErrorListDepartmentResource) {
                errors.rejectValue("departmentResource", objectError.getCode());
            }
        }

        CurrencyResource currencyResource = salePriceResource.getCurrencyResource();
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

        if (salePriceResource.getDate() == null ||
                salePriceResource.getDate().trim().isEmpty()) {
            errors.rejectValue("date", SalePriceDateEmptyMessageCode);
        } else {
            try {
                String dateText = salePriceResource.getDate();
                this.conversionService.convert(dateText, Date.class);
                salePriceResource.setDate(salePriceResource.getDate().trim());
            } catch (IllegalArgumentException illegalArgumentException) {
                errors.rejectValue("date", SalePriceDateInvalidMessageCode);
            }
        }

        if (salePriceResource.getPrice() == null ||
                salePriceResource.getPrice().trim().isEmpty()) {
            errors.rejectValue ("price", SalePricePriceEmptyMessageCode);
        } else {
            try {
                String priceText = salePriceResource.getPrice();
                Double price = this.conversionService.convert(priceText, Double.class);
                if (price < 0.00) {
                    errors.rejectValue("price", SalePricePriceNegativeMessageCode);
                } else if (price == 0.00) {
                    errors.rejectValue("price", SalePricePriceEqualsZeroMessageCode);
                } else {
                    salePriceResource.setPrice(salePriceResource.getPrice().trim());
                }
            } catch (IllegalArgumentException illegalArgumentException) {
                errors.rejectValue("price", SalePricePriceInvalidMessageCode);
            }
        }

        if (salePriceResource.getQuantity() == null ||
                salePriceResource.getQuantity().trim().isEmpty()) {
            errors.rejectValue("quantity", SalePriceQuantityEmptyMessageCode);
        } else {
            try {
                String quantityText = salePriceResource.getQuantity();
                Double quantity = this.conversionService.convert(quantityText, Double.class);
                if (quantity < 0.000) {
                    errors.rejectValue("quantity", SalePriceQuantityNegativeMessageCode);
                } else if (quantity == 0.000) {
                    errors.rejectValue("quantity", SalePriceQuantityEqualsZeroMessageCode);
                } else {
                    salePriceResource.setQuantity(salePriceResource.getQuantity().trim());
                }
            } catch (NumberFormatException numberFormatException) {
                errors.rejectValue("quantity", SalePriceQuantityInvalidMessageCode);
            }
        }
    }
}