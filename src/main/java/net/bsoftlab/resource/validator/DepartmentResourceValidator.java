package net.bsoftlab.resource.validator;

import net.bsoftlab.resource.AddressResource;
import net.bsoftlab.resource.DepartmentResource;

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
public class DepartmentResourceValidator implements Validator {

    private static final String DepartmentEmptyMessageCode = "department.empty";
    private static final String DepartmentInvalidMessageCode = "department.invalid";
    private static final String DepartmentCodeEmptyMessageCode = "department.code.empty";
    private static final String DepartmentCodeSizeInvalidMessageCode = "department.code.size.invalid";
    private static final String DepartmentNameEmptyMessageCode = "department.name.empty";
    private static final String DepartmentNameSizeInvalidMessageCode = "department.name.size.invalid";
    private static final String DepartmentPhonesEmptyMessageCode = "department.phones.empty";
    private static final String DepartmentPhonesSizeInvalidMessageCode = "department.phones.size.invalid";
    private static final String DepartmentFaxesEmptyMessageCode = "department.faxes.empty";
    private static final String DepartmentFaxesSizeInvalidMessageCode = "department.faxes.size.invalid";
    private static final String DepartmentWebSiteEmptyMessageCode = "department.webSite.empty";
    private static final String DepartmentWebSiteSizeInvalidMessageCode = "department.webSite.size.invalid";
    private static final String DepartmentEmailAddressEmptyMessageCode = "department.emailAddress.empty";
    private static final String DepartmentEmailAddressSizeInvalidMessageCode = "department.emailAddress.size.invalid";
    private static final String DepartmentAdditionalInformationSizeInvalidMessageCode = "department.additionalInformation.size.invalid";

    private AddressResourceValidator addressResourceValidator = null;

    @Autowired
    public void setAddressResourceValidator(
            AddressResourceValidator addressResourceValidator) {
        this.addressResourceValidator = addressResourceValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return DepartmentResource.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object resource, Errors errors) {
        if(resource == null) {
            errors.reject(DepartmentEmptyMessageCode);
            return;
        }
        DepartmentResource departmentResource;
        try {
            departmentResource = (DepartmentResource) resource;
        } catch (ClassCastException classCastException) {
            errors.reject(DepartmentInvalidMessageCode);
            return;
        }

        if (departmentResource.getCode() == null ||
                departmentResource.getCode().trim().isEmpty()) {
            errors.rejectValue("code", DepartmentCodeEmptyMessageCode);
        } else if (departmentResource.getCode().trim().length() != 8) {
            errors.rejectValue("code", DepartmentCodeSizeInvalidMessageCode);
        } else {
            departmentResource.setCode(departmentResource.getCode().trim());
        }

        if (departmentResource.getName() == null ||
                departmentResource.getName().trim().isEmpty()) {
            errors.rejectValue("name", DepartmentNameEmptyMessageCode);
        } else if (departmentResource.getName().trim().length() > 100) {
            errors.rejectValue("name", DepartmentNameSizeInvalidMessageCode);
        } else {
            departmentResource.setName(departmentResource.getName().trim());
        }

        AddressResource addressResource = departmentResource.getAddressResource();
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

        if (departmentResource.getPhones() == null ||
                departmentResource.getPhones().trim().isEmpty()) {
            errors.rejectValue("phones", DepartmentPhonesEmptyMessageCode);
        } else if (departmentResource.getPhones().trim().length() > 100) {
            errors.rejectValue("phones", DepartmentPhonesSizeInvalidMessageCode);
        } else {
            departmentResource.setPhones(departmentResource.getPhones().trim());
        }

        if (departmentResource.getFaxes() == null ||
                departmentResource.getFaxes().trim().isEmpty()) {
            errors.rejectValue("faxes", DepartmentFaxesEmptyMessageCode);
        } else if (departmentResource.getFaxes().trim().length() > 100) {
            errors.rejectValue("faxes", DepartmentFaxesSizeInvalidMessageCode);
        } else {
            departmentResource.setFaxes(departmentResource.getFaxes().trim());
        }

        if (departmentResource.getWebSite() == null ||
                departmentResource.getWebSite().trim().isEmpty()) {
            errors.rejectValue("webSite", DepartmentWebSiteEmptyMessageCode);
        } else if (departmentResource.getWebSite().trim().length() > 100) {
            errors.rejectValue("webSite", DepartmentWebSiteSizeInvalidMessageCode);
        } else {
            departmentResource.setWebSite(departmentResource.getWebSite().trim());
        }

        if (departmentResource.getEmailAddress() == null ||
                departmentResource.getEmailAddress().trim().isEmpty()) {
            errors.rejectValue("emailAddress", DepartmentEmailAddressEmptyMessageCode);
        } else if (departmentResource.getEmailAddress().trim().length() > 100) {
            errors.rejectValue("emailAddress", DepartmentEmailAddressSizeInvalidMessageCode);
        } else {
            departmentResource.setEmailAddress(departmentResource.getEmailAddress().trim());
        }

        if (departmentResource.getAdditionalInformation() != null &&
                departmentResource.getAdditionalInformation().trim().length() > 500) {
            errors.rejectValue("additionalInformation",
                    DepartmentAdditionalInformationSizeInvalidMessageCode);
        }
    }
}