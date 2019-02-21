package net.bsoftlab.resource.assembler;

import net.bsoftlab.model.Currency;
import net.bsoftlab.controller.rest.CurrencyRestController;
import net.bsoftlab.resource.CurrencyResource;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CurrencyResourceAssembler extends
        ResourceAssemblerSupport<Currency, CurrencyResource> {

    public CurrencyResourceAssembler(
            Class<CurrencyRestController> currencyRestControllerClass,
            Class<CurrencyResource> currencyResourceClass) {
        super(currencyRestControllerClass, currencyResourceClass);
    }
    public CurrencyResourceAssembler(){
        this(CurrencyRestController.class,
                CurrencyResource.class);
    }

    @Override
    public CurrencyResource toResource(Currency currency) {
        if (currency == null) {
            return null;
        }
        CurrencyResource currencyResource = this.instantiateResource(currency);
        currencyResource.setCode(currency.getCode());
        currencyResource.setShortName(currency.getShortName());
        currencyResource.setLongName(currency.getLongName());
        currencyResource.setCountry(currency.getCountry());
        currencyResource.setAdditionalInformation(currency.getAdditionalInformation());
        currencyResource.add(ControllerLinkBuilder.linkTo(
                CurrencyRestController.class).slash(currency.getCode()).withSelfRel());
        return currencyResource;
    }

    public List<CurrencyResource> toResources(List<Currency> currencyList) {
        if (currencyList == null) {
            return null;
        }
        return super.toResources(currencyList);
    }
}