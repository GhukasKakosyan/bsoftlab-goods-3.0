package net.bsoftlab.resource.converter.entity;

import net.bsoftlab.model.Address;
import net.bsoftlab.resource.AddressResource;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AddressConverter implements Converter<AddressResource, Address> {

    @Override
    public Address convert(AddressResource addressResource) {
        if (addressResource == null) {
            return null;
        }
        Address address = new Address();
        address.setStreet(addressResource.getStreet());
        address.setPincode(addressResource.getPincode());
        address.setCity(addressResource.getCity());
        address.setState(addressResource.getState());
        address.setCountry(addressResource.getCountry());
        return address;
    }
}
