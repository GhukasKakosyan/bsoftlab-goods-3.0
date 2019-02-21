package net.bsoftlab.resource.assembler;

import net.bsoftlab.model.Address;
import net.bsoftlab.resource.AddressResource;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AddressResourceAssembler {

    public AddressResource toResource(Address address) {
        if (address == null) {
            return null;
        }
        AddressResource addressResource = new AddressResource();
        addressResource.setStreet(address.getStreet());
        addressResource.setPincode(address.getPincode());
        addressResource.setCity(address.getCity());
        addressResource.setState(address.getState());
        addressResource.setCountry(address.getCountry());
        return addressResource;
    }
}
