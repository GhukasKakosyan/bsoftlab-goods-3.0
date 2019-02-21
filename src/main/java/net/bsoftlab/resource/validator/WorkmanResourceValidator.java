package net.bsoftlab.resource.validator;

import net.bsoftlab.resource.AddressResource;
import net.bsoftlab.resource.RoleResource;
import net.bsoftlab.resource.WorkmanResource;

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
import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WorkmanResourceValidator implements Validator {

    private static final String WorkmanEmptyMessageCode = "workman.empty";
    private static final String WorkmanInvalidMessageCode = "workman.invalid";
    private static final String WorkmanIdNegativeMessageCode = "workman.id.negative";
    private static final String WorkmanIdInvalidMessageCode = "workman.id.invalid";
    private static final String WorkmanNameEmptyMessageCode = "workman.name.empty";
    private static final String WorkmanNameSizeInvalidMessageCode = "workman.name.size.invalid";
    private static final String WorkmanPasswordEmptyMessageCode = "workman.password.empty";
    private static final String WorkmanPasswordSizeInvalidMessageCode = "workman.password.size.invalid";
    private static final String WorkmanFirstNameEmptyMessageCode = "workman.firstName.empty";
    private static final String WorkmanFirstNameSizeInvalidMessageCode = "workman.firstName.size.invalid";
    private static final String WorkmanLastNameEmptyMessageCode = "workman.lastName.empty";
    private static final String WorkmanLastNameSizeInvalidMessageCode = "workman.lastName.size.invalid";
    private static final String WorkmanPhonesEmptyMessageCode = "workman.phones.empty";
    private static final String WorkmanPhonesSizeInvalidMessageCode = "workman.phones.size.invalid";

    private ConversionService conversionService = null;
    private AddressResourceValidator addressResourceValidator = null;
    private RoleResourceValidator roleResourceValidator = null;

    @Autowired
    public void setConversionService(
            ConversionService conversionService) {
        this.conversionService = conversionService;
    }
    @Autowired
    public void setAddressResourceValidator(
            AddressResourceValidator addressResourceValidator) {
        this.addressResourceValidator = addressResourceValidator;
    }
    @Autowired
    public void setRoleResourceValidator(
            RoleResourceValidator roleResourceValidator) {
        this.roleResourceValidator = roleResourceValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return WorkmanResource.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object resource, Errors errors) {
        if (resource == null) {
            errors.reject(WorkmanEmptyMessageCode);
            return;
        }
        WorkmanResource workmanResource;
        try {
            workmanResource = (WorkmanResource) resource;
        } catch (ClassCastException classCastException) {
            errors.reject(WorkmanInvalidMessageCode);
            return;
        }

        String idText = workmanResource.getCode();
        if (idText != null && !idText.trim().isEmpty()) {
            try {
                if (this.conversionService.convert(idText, Integer.class) < 0) {
                    errors.rejectValue("code", WorkmanIdNegativeMessageCode);
                } else {
                    workmanResource.setCode(workmanResource.getCode().trim());
                }
            } catch (IllegalArgumentException illegalArgumentException) {
                errors.rejectValue("code", WorkmanIdInvalidMessageCode);
            }
        }

        if (workmanResource.getName() == null ||
                workmanResource.getName().trim().isEmpty()) {
            errors.rejectValue("name", WorkmanNameEmptyMessageCode);
        } else if (workmanResource.getName().trim().length() > 255) {
            errors.rejectValue("name", WorkmanNameSizeInvalidMessageCode);
        } else {
            workmanResource.setName(workmanResource.getName().trim());
        }

        if (workmanResource.getPassword() == null ||
                workmanResource.getPassword().trim().isEmpty()) {
            errors.rejectValue("password", WorkmanPasswordEmptyMessageCode);
        } else if (workmanResource.getPassword().trim().length() > 255) {
            errors.rejectValue("password", WorkmanPasswordSizeInvalidMessageCode);
        } else {
            workmanResource.setPassword(workmanResource.getPassword().trim());
        }

        if (workmanResource.getFirstName() == null ||
                workmanResource.getFirstName().trim().isEmpty()) {
            errors.rejectValue("firstName", WorkmanFirstNameEmptyMessageCode);
        } else if (workmanResource.getFirstName().trim().length() > 100) {
            errors.rejectValue("firstName", WorkmanFirstNameSizeInvalidMessageCode);
        } else {
            workmanResource.setFirstName(workmanResource.getFirstName().trim());
        }

        if (workmanResource.getLastName() == null ||
                workmanResource.getLastName().trim().isEmpty()) {
            errors.rejectValue("lastName", WorkmanLastNameEmptyMessageCode);
        } else if (workmanResource.getLastName().trim().length() > 100) {
            errors.rejectValue("lastName", WorkmanLastNameSizeInvalidMessageCode);
        } else {
            workmanResource.setLastName(workmanResource.getLastName().trim());
        }

        if (workmanResource.getPhones() == null ||
                workmanResource.getPhones().trim().isEmpty()) {
            errors.rejectValue("phones", WorkmanPhonesEmptyMessageCode);
        } else if (workmanResource.getPhones().trim().length() > 100) {
            errors.rejectValue("phones", WorkmanPhonesSizeInvalidMessageCode);
        } else {
            workmanResource.setPhones(workmanResource.getPhones().trim());
        }

        AddressResource addressResource = workmanResource.getAddressResource();
        BeanPropertyBindingResult beanPropertyBindingResultAddressResource =
                new BeanPropertyBindingResult(addressResource, "addressResource");
        ValidationUtils.invokeValidator(this.addressResourceValidator,
                addressResource, beanPropertyBindingResultAddressResource);
        List<ObjectError> objectErrorListAddressResource =
                beanPropertyBindingResultAddressResource.getAllErrors();
        if (objectErrorListAddressResource != null &&
                !objectErrorListAddressResource.isEmpty()) {
            for (ObjectError objectError : objectErrorListAddressResource) {
                errors.rejectValue("addressResource", objectError.getCode());
            }
        }

        RoleResource roleResource = workmanResource.getRoleResource();
        BeanPropertyBindingResult beanPropertyBindingResultRoleResource =
                new BeanPropertyBindingResult(roleResource, "roleResource");
        ValidationUtils.invokeValidator(this.roleResourceValidator,
                roleResource, beanPropertyBindingResultRoleResource);
        List<ObjectError> objectErrorListRoleResource =
                beanPropertyBindingResultRoleResource.getAllErrors();
        if (objectErrorListRoleResource != null &&
                !objectErrorListRoleResource.isEmpty()) {
            for (ObjectError objectError : objectErrorListRoleResource) {
                errors.rejectValue("roleResource", objectError.getCode());
            }
        }
    }
}