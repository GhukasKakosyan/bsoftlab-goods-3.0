package net.bsoftlab.resource.assembler;

import net.bsoftlab.model.SalePrice;
import net.bsoftlab.controller.rest.SalePriceRestController;
import net.bsoftlab.resource.CurrencyResource;
import net.bsoftlab.resource.DepartmentResource;
import net.bsoftlab.resource.MatvalueResource;
import net.bsoftlab.resource.SalePriceResource;

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
public class SalePriceResourceAssembler extends
        ResourceAssemblerSupport<SalePrice, SalePriceResource> {

    private ConversionService conversionService = null;
    private CurrencyResourceAssembler currencyResourceAssembler = null;
    private DepartmentResourceAssembler departmentResourceAssembler = null;
    private MatvalueResourceAssembler matvalueResourceAssembler = null;

    public SalePriceResourceAssembler(
            Class<SalePriceRestController> salePriceRestControllerClass,
            Class<SalePriceResource> salePriceResourceClass) {
        super(salePriceRestControllerClass, salePriceResourceClass);
    }
    public SalePriceResourceAssembler() {
        this(SalePriceRestController.class,
                SalePriceResource.class);
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
    @Autowired
    public void setDepartmentResourceAssembler(
            DepartmentResourceAssembler departmentResourceAssembler) {
        this.departmentResourceAssembler = departmentResourceAssembler;
    }
    @Autowired
    public void setMatvalueResourceAssembler(
            MatvalueResourceAssembler matvalueResourceAssembler) {
        this.matvalueResourceAssembler = matvalueResourceAssembler;
    }

    @Override
    public SalePriceResource toResource(SalePrice salePrice) {
        if (salePrice == null) {
            return null;
        }
        CurrencyResource currencyResource =
                this.currencyResourceAssembler.toResource(salePrice.getCurrency());
        DepartmentResource departmentResource =
                this.departmentResourceAssembler.toResource(salePrice.getDepartment());
        MatvalueResource matvalueResource =
                this.matvalueResourceAssembler.toResource(salePrice.getMatvalue());

        Integer ID = salePrice.getID();
        String code = this.conversionService.convert(ID, String.class);
        Date date = salePrice.getDate();
        String dateText = this.conversionService.convert(date, String.class);
        Double price = salePrice.getPrice();
        String priceText = this.conversionService.convert(price, String.class);
        Double quantity = salePrice.getQuantity();
        String quantityText = this.conversionService.convert(quantity, String.class);

        SalePriceResource salePriceResource = this.instantiateResource(salePrice);
        salePriceResource.setCode(code);
        salePriceResource.setMatvalueResource(matvalueResource);
        salePriceResource.setDepartmentResource(departmentResource);
        salePriceResource.setCurrencyResource(currencyResource);
        salePriceResource.setDate(dateText);
        salePriceResource.setPrice(priceText);
        salePriceResource.setQuantity(quantityText);
        salePriceResource.add(ControllerLinkBuilder
                .linkTo(SalePriceRestController.class)
                .slash(salePriceResource.getCode()).withSelfRel());
        return salePriceResource;
    }

    public List<SalePriceResource> toResources(List<SalePrice> salePriceList) {
        if (salePriceList == null) {
            return null;
        }
        return super.toResources(salePriceList);
    }
}