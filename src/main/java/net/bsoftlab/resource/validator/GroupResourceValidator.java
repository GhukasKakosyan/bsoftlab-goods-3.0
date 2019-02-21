package net.bsoftlab.resource.validator;

import net.bsoftlab.resource.GroupResource;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class GroupResourceValidator implements Validator {

    private static final String GroupEmptyMessageCode = "group.empty";
    private static final String GroupInvalidMessageCode = "group.invalid";
    private static final String GroupCodeEmptyMessageCode = "group.code.empty";
    private static final String GroupCodeSizeInvalidMessageCode = "group.code.size.invalid";
    private static final String GroupNameEmptyMessageCode = "group.name.empty";
    private static final String GroupNameSizeInvalidMessageCode = "group.name.size.invalid";
    private static final String GroupEnhancedNameEmptyMessageCode = "group.enhancedName.empty";
    private static final String GroupEnhancedNameSizeInvalidMessageCode = "group.enhancedName.size.invalid";

    @Override
    public boolean supports(Class<?> clazz) {
        return GroupResource.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object resource, Errors errors) {
        if(resource == null) {
            errors.reject(GroupEmptyMessageCode);
            return;
        }
        GroupResource groupResource;
        try {
            groupResource = (GroupResource) resource;
        } catch (ClassCastException classCastException) {
            errors.reject(GroupInvalidMessageCode);
            return;
        }

        if (groupResource.getCode() == null ||
                groupResource.getCode().trim().isEmpty()) {
            errors.rejectValue("code", GroupCodeEmptyMessageCode);
        } else if (groupResource.getCode().trim().length() != 8) {
            errors.rejectValue("code", GroupCodeSizeInvalidMessageCode);
        } else {
            groupResource.setCode(groupResource.getCode().trim());
        }

        if (groupResource.getName() == null ||
                groupResource.getName().trim().isEmpty()) {
            errors.rejectValue("name", GroupNameEmptyMessageCode);
        } else if (groupResource.getName().trim().length() > 100) {
            errors.rejectValue("name", GroupNameSizeInvalidMessageCode);
        } else {
            groupResource.setName(groupResource.getName().trim());
        }

        if (groupResource.getEnhancedName() == null ||
                groupResource.getEnhancedName().trim().isEmpty()) {
            errors.rejectValue("enhancedName", GroupEnhancedNameEmptyMessageCode);
        } else if (groupResource.getEnhancedName().trim().length() > 200) {
            errors.rejectValue("enhancedName", GroupEnhancedNameSizeInvalidMessageCode);
        } else {
            groupResource.setEnhancedName(groupResource.getEnhancedName().trim());
        }
    }
}