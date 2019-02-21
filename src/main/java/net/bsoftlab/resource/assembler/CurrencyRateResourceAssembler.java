package net.bsoftlab.resource.assembler;

import net.bsoftlab.model.CurrencyRate;
import net.bsoftlab.controller.rest.CurrencyRateRestController;
import net.bsoftlab.resource.CurrencyRateResource;
import net.bsoftlab.resource.CurrencyResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.ConversionService;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CurrencyRateResourceAssembler extends
        ResourceAssemblerSupport<CurrencyRate, CurrencyRateResource> {

    private ConversionService conversionService = null;
    private CurrencyResourceAssembler currencyResourceAssembler = null;

    public CurrencyRateResourceAssembler(
            Class<CurrencyRateRestController> currencyRateRestControllerClass,
            Class<CurrencyRateResource> currencyRateResourceClass) {
        super(currencyRateRestControllerClass, currencyRateResourceClass);
    }
    public CurrencyRateResourceAssembler() {
        this(CurrencyRateRestController.class,
                CurrencyRateResource.class);
    }

    @Autowired
    public void setConversionService(
            ConversionService conversionService) {
        this.conversionService = conversionService;
    }
    @Autowired
    public void setCurrencyResourceAssembler(
            CurrencyResourceAssembler currencyResourceAssembler) {
        this.currencyResourceAssembler = currencyResourceAssembler;
    }

    @Override
    public CurrencyRateResource toResource(CurrencyRate currencyRate) {
        if (currencyRate == null) {
            return null;
        }
        Integer ID = currencyRate.getID();
        String code = this.conversionService.convert(ID, String.class);

        Date date = currencyRate.getDate();
        String dateText = this.conversionService.convert(date, String.class);

        Double rate = currencyRate.getRate();
        String rateText = this.conversionService.convert(rate, String.class);

        Double quantity = currencyRate.getQuantity();
        String quantityText = this.conversionService.convert(quantity, String.class);

        CurrencyResource currencyResource =
                this.currencyResourceAssembler.toResource(currencyRate.getCurrency());

        CurrencyRateResource currencyRateResource = this.instantiateResource(currencyRate);
        currencyRateResource.setCode(code);
        currencyRateResource.setCurrencyResource(currencyResource);
        currencyRateResource.setDate(dateText);
        currencyRateResource.setRate(rateText);
        currencyRateResource.setQuantity(quantityText);
        currencyRateResource.add(ControllerLinkBuilder.linkTo(CurrencyRateRestController.class)
                .slash(currencyRateResource.getCode()).withSelfRel());
        return currencyRateResource;
    }

    public List<CurrencyRateResource> toResources(List<CurrencyRate> currencyRateList) {
        if (currencyRateList == null) {
            return null;
        }
        return super.toResources(currencyRateList);
    }
}