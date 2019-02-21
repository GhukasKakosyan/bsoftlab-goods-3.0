package net.bsoftlab.resource.validator;

import net.bsoftlab.resource.RoleResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RoleResourceValidator implements Validator {

    private static final String RoleEmptyMessageCode = "role.empty";
    private static final String RoleInvalidMessageCode = "role.invalid";
    private static final String RoleIdNegativeMessageCode = "role.id.negative";
    private static final String RoleIdInvalidMessageCode = "role.id.invalid";
    private static final String RoleNameEmptyMessageCode = "role.name.empty";
    private static final String RoleNameSizeInvalidMessageCode = "role.name.size.invalid";

    private ConversionService conversionService = null;

    @Autowired
    public void setConversionService(
            ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return RoleResource.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object resource, Errors errors) {
        if(resource == null) {
            errors.reject(RoleEmptyMessageCode);
            return;
        }
        RoleResource roleResource;
        try {
            roleResource = (RoleResource) resource;
        } catch (ClassCastException classCastException) {
            errors.reject(RoleInvalidMessageCode);
            return;
        }

        String idText = roleResource.getCode();
        if (idText != null && !idText.trim().isEmpty()) {
            try {
                if (this.conversionService.convert(idText, Integer.class) < 0) {
                    errors.rejectValue("code", RoleIdNegativeMessageCode);
                } else {
                    roleResource.setCode(roleResource.getCode().trim());
                }
            } catch (IllegalArgumentException illegalArgumentException) {
                errors.rejectValue("code", RoleIdInvalidMessageCode);
            }
        }

        if (roleResource.getName() == null || roleResource.getName().trim().isEmpty()) {
            errors.rejectValue("name", RoleNameEmptyMessageCode);
        } else if (roleResource.getName().trim().length() > 50) {
            errors.rejectValue("name", RoleNameSizeInvalidMessageCode);
        } else {
            roleResource.setName(roleResource.getName().trim());
        }
    }
}