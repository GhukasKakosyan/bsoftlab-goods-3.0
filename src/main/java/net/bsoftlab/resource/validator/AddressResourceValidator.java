package net.bsoftlab.resource.validator;

import net.bsoftlab.resource.AddressResource;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AddressResourceValidator implements Validator {

    private static final String AddressEmptyMessageCode = "address.empty";
    private static final String AddressInvalidMessageCode = "address.invalid";
    private static final String AddressStreetEmptyMessageCode = "address.street.empty";
    private static final String AddressStreetSizeInvalidMessageCode = "address.street.size.invalid";
    private static final String AddressPincodeEmptyMessageCode = "address.pincode.empty";
    private static final String AddressPincodeSizeInvalidMessageCode = "address.pincode.size.invalid";
    private static final String AddressCityEmptyMessageCode = "address.city.empty";
    private static final String AddressCitySizeInvalidMessageCode = "address.city.size.invalid";
    private static final String AddressStateEmptyMessageCode = "address.state.empty";
    private static final String AddressStateSizeInvalidMessageCode = "address.state.size.invalid";
    private static final String AddressCountryEmptyMessageCode = "address.country.empty";
    private static final String AddressCountrySizeInvalidMessageCode = "address.country.size.invalid";

    @Override
    public boolean supports(Class<?> clazz) {
        return AddressResource.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object resource, Errors errors) {
        if(resource == null) {
            errors.reject(AddressEmptyMessageCode);
            return;
        }
        AddressResource addressResource;
        try {
            addressResource = (AddressResource) resource;
        } catch (ClassCastException classCastException) {
            errors.reject(AddressInvalidMessageCode);
            return;
        }

        if (addressResource.getStreet() == null ||
                addressResource.getStreet().trim().isEmpty()) {
            errors.rejectValue("street", AddressStreetEmptyMessageCode);
        } else if (addressResource.getStreet().trim().length() > 100) {
            errors.rejectValue("street", AddressStreetSizeInvalidMessageCode);
        } else {
            addressResource.setStreet(addressResource.getStreet().trim());
        }

        if (addressResource.getPincode() == null ||
                addressResource.getPincode().trim().isEmpty()) {
            errors.rejectValue("pincode", AddressPincodeEmptyMessageCode);
        } else if (addressResource.getPincode().trim().length() > 20) {
            errors.rejectValue("pincode", AddressPincodeSizeInvalidMessageCode);
        } else {
            addressResource.setPincode(addressResource.getPincode().trim());
        }

        if (addressResource.getCity() == null ||
                addressResource.getCity().trim().isEmpty()) {
            errors.rejectValue("city", AddressCityEmptyMessageCode);
        } else if (addressResource.getCity().trim().length() > 100) {
            errors.rejectValue("city", AddressCitySizeInvalidMessageCode);
        } else {
            addressResource.setCity(addressResource.getCity().trim());
        }

        if (addressResource.getState() == null ||
                addressResource.getState().trim().isEmpty()) {
            errors.rejectValue("state", AddressStateEmptyMessageCode);
        } else if (addressResource.getState().trim().length() > 100) {
            errors.rejectValue("state", AddressStateSizeInvalidMessageCode);
        } else {
            addressResource.setState(addressResource.getState().trim());
        }

        if (addressResource.getCountry() == null ||
                addressResource.getCountry().trim().isEmpty()) {
            errors.rejectValue("country", AddressCountryEmptyMessageCode);
        } else if (addressResource.getCountry().trim().length() > 100) {
            errors.rejectValue("country", AddressCountrySizeInvalidMessageCode);
        } else {
            addressResource.setCountry(addressResource.getCountry().trim());
        }
    }
}