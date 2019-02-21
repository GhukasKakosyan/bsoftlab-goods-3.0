package net.bsoftlab.resource.validator;

import net.bsoftlab.resource.GroupResource;
import net.bsoftlab.resource.MatvalueResource;
import net.bsoftlab.resource.UnitofmsrResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MatvalueResourceValidator implements Validator {

    private static final String MatvalueEmptyMessageCode = "matvalue.empty";
    private static final String MatvalueInvalidMessageCode = "matvalue.invalid";
    private static final String MatvalueCodeEmptyMessageCode = "matvalue.code.empty";
    private static final String MatvalueCodeSizeInvalidMessageCode = "matvalue.code.size.invalid";
    private static final String MatvalueNameEmptyMessageCode = "matvalue.name.empty";
    private static final String MatvalueNameSizeInvalidMessageCode = "matvalue.name.size.invalid";

    private GroupResourceValidator groupResourceValidator = null;
    private UnitofmsrResourceValidator unitofmsrResourceValidator = null;

    @Autowired
    public void setGroupResourceValidator(
            GroupResourceValidator groupResourceValidator) {
        this.groupResourceValidator = groupResourceValidator;
    }
    @Autowired
    public void setUnitofmsrResourceValidator(
            UnitofmsrResourceValidator unitofmsrResourceValidator) {
        this.unitofmsrResourceValidator = unitofmsrResourceValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return MatvalueResource.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object resource, Errors errors) {
        if(resource == null) {
            errors.reject(MatvalueEmptyMessageCode);
            return;
        }
        MatvalueResource matvalueResource;
        try {
            matvalueResource = (MatvalueResource) resource;
        } catch (ClassCastException classCastException) {
            errors.reject(MatvalueInvalidMessageCode);
            return;
        }

        if (matvalueResource.getCode() == null ||
                matvalueResource.getCode().trim().isEmpty()) {
            errors.rejectValue("code", MatvalueCodeEmptyMessageCode);
        } else if (matvalueResource.getCode().trim().length() != 13) {
            errors.rejectValue("code", MatvalueCodeSizeInvalidMessageCode);
        } else {
            matvalueResource.setCode(matvalueResource.getCode().trim());
        }

        if (matvalueResource.getName() == null ||
                matvalueResource.getName().trim().isEmpty()) {
            errors.rejectValue("name", MatvalueNameEmptyMessageCode);
        } else if (matvalueResource.getName().trim().length() > 100) {
            errors.rejectValue("name", MatvalueNameSizeInvalidMessageCode);
        } else {
            matvalueResource.setName(matvalueResource.getName().trim());
        }

        UnitofmsrResource unitofmsrResource = matvalueResource.getUnitofmsrResource();
        BeanPropertyBindingResult beanPropertyBindingResultUnitofmsrResource =
                new BeanPropertyBindingResult(unitofmsrResource, "unitofmsrResource");
        ValidationUtils.invokeValidator(this.unitofmsrResourceValidator,
                unitofmsrResource, beanPropertyBindingResultUnitofmsrResource);
        List<ObjectError> objectErrorListUnitofmsrResource =
                beanPropertyBindingResultUnitofmsrResource.getAllErrors();
        if (objectErrorListUnitofmsrResource != null &&
                !objectErrorListUnitofmsrResource.isEmpty()) {
            for (ObjectError objectError : objectErrorListUnitofmsrResource) {
                errors.rejectValue("unitofmsrResource", objectError.getCode());
            }
        }

        GroupResource groupResource = matvalueResource.getGroupResource();
        BeanPropertyBindingResult beanPropertyBindingResultGroupResource =
                new BeanPropertyBindingResult(groupResource, "groupResource");
        ValidationUtils.invokeValidator(this.groupResourceValidator,
                groupResource, beanPropertyBindingResultGroupResource);
        List<ObjectError> objectErrorListGroupResource =
                beanPropertyBindingResultGroupResource.getAllErrors();
        if (objectErrorListGroupResource != null &&
                !objectErrorListGroupResource.isEmpty()) {
            for (ObjectError objectError : objectErrorListGroupResource) {
                errors.rejectValue("groupResource", objectError.getCode());
            }
        }
    }
}