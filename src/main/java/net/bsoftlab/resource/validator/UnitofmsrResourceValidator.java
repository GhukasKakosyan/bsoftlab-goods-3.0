package net.bsoftlab.resource.validator;

import net.bsoftlab.resource.UnitofmsrResource;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UnitofmsrResourceValidator implements Validator {

    private static final String UnitofmsrEmptyMessageCode = "unitofmsr.empty";
    private static final String UnitofmsrInvalidMessageCode = "unitofmsr.invalid";
    private static final String UnitofmsrCodeEmptyMessageCode = "unitofmsr.code.empty";
    private static final String UnitofmsrCodeSizeInvalidMessageCode = "unitofmsr.code.size.invalid";
    private static final String UnitofmsrShortNameEmptyMessageCode = "unitofmsr.shortName.empty";
    private static final String UnitofmsrShortNameSizeInvalidMessageCode = "unitofmsr.shortName.size.invalid";
    private static final String UnitofmsrLongNameEmptyMessageCode = "unitofmsr.longName.empty";
    private static final String UnitofmsrLongNameSizeInvalidMessageCode = "unitofmsr.longName.size.invalid";

    @Override
    public boolean supports(Class<?> clazz) {
        return UnitofmsrResource.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object resource, Errors errors) {
        if(resource == null) {
            errors.reject(UnitofmsrEmptyMessageCode);
            return;
        }
        UnitofmsrResource unitofmsrResource;
        try {
            unitofmsrResource = (UnitofmsrResource) resource;
        } catch (ClassCastException classCastException) {
            errors.reject(UnitofmsrInvalidMessageCode);
            return;
        }

        if (unitofmsrResource.getCode() == null ||
                unitofmsrResource.getCode().trim().isEmpty()) {
            errors.rejectValue("code", UnitofmsrCodeEmptyMessageCode);
        } else if (unitofmsrResource.getCode().trim().length() != 3) {
            errors.rejectValue("code", UnitofmsrCodeSizeInvalidMessageCode);
        } else {
            unitofmsrResource.setCode(unitofmsrResource.getCode().trim());
        }

        if (unitofmsrResource.getShortName() == null ||
                unitofmsrResource.getShortName().trim().isEmpty()) {
            errors.rejectValue("shortName", UnitofmsrShortNameEmptyMessageCode);
        } else if (unitofmsrResource.getShortName().trim().length() > 20) {
            errors.rejectValue("shortName", UnitofmsrShortNameSizeInvalidMessageCode);
        } else {
            unitofmsrResource.setShortName(unitofmsrResource.getShortName().trim());
        }

        if (unitofmsrResource.getLongName() == null ||
                unitofmsrResource.getLongName().trim().isEmpty()) {
            errors.rejectValue("longName", UnitofmsrLongNameEmptyMessageCode);
        } else if (unitofmsrResource.getLongName().trim().length() > 100) {
            errors.rejectValue("longName", UnitofmsrLongNameSizeInvalidMessageCode);
        } else {
            unitofmsrResource.setLongName(unitofmsrResource.getLongName());
        }
    }
}