package net.bsoftlab.resource.converter.entity;

import net.bsoftlab.model.Currency;
import net.bsoftlab.model.Department;
import net.bsoftlab.model.Matvalue;
import net.bsoftlab.model.SalePrice;

import net.bsoftlab.resource.CurrencyResource;
import net.bsoftlab.resource.DepartmentResource;
import net.bsoftlab.resource.MatvalueResource;
import net.bsoftlab.resource.SalePriceResource;
import net.bsoftlab.resource.converter.type.StringToDateConverter;
import net.bsoftlab.resource.converter.type.StringToDoubleConverter;
import net.bsoftlab.resource.converter.type.StringToIntegerConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SalePriceConverter implements Converter<SalePriceResource, SalePrice> {

    private CurrencyConverter currencyConverter = null;
    private DepartmentConverter departmentConverter = null;
    private MatvalueConverter matvalueConverter = null;

    private StringToDateConverter stringToDateConverter = null;
    private StringToDoubleConverter stringToDoubleConverter = null;
    private StringToIntegerConverter stringToIntegerConverter = null;

    @Autowired
    public void setCurrencyConverter(
            CurrencyConverter currencyConverter) {
        this.currencyConverter = currencyConverter;
    }
    @Autowired
    public void setDepartmentConverter(
            DepartmentConverter departmentConverter) {
        this.departmentConverter = departmentConverter;
    }
    @Autowired
    public void setMatvalueConverter(
            MatvalueConverter matvalueConverter) {
        this.matvalueConverter = matvalueConverter;
    }

    @Autowired
    public void setStringToDateConverter(
            StringToDateConverter stringToDateConverter) {
        this.stringToDateConverter = stringToDateConverter;
    }
    @Autowired
    public void setStringToDoubleConverter(
            StringToDoubleConverter stringToDoubleConverter) {
        this.stringToDoubleConverter = stringToDoubleConverter;
    }
    @Autowired
    public void setStringToIntegerConverter(
            StringToIntegerConverter stringToIntegerConverter) {
        this.stringToIntegerConverter = stringToIntegerConverter;
    }

    @Override
    public SalePrice convert(SalePriceResource salePriceResource) {
        if (salePriceResource == null) {
            return null;
        }
        Integer ID;
        String code = salePriceResource.getCode();
        if (code == null || code.trim().isEmpty()) {
            ID = null;
        } else {
            ID = this.stringToIntegerConverter.convert(code);
        }

        String dateText = salePriceResource.getDate().trim();
        Date date = this.stringToDateConverter.convert(dateText);

        String priceText = salePriceResource.getPrice();
        Double price = this.stringToDoubleConverter.convert(priceText);

        String quantityText = salePriceResource.getQuantity();
        Double quantity = this.stringToDoubleConverter.convert(quantityText);

        CurrencyResource currencyResource = salePriceResource.getCurrencyResource();
        Currency currency = this.currencyConverter.convert(currencyResource);

        DepartmentResource departmentResource = salePriceResource.getDepartmentResource();
        Department department = this.departmentConverter.convert(departmentResource);

        MatvalueResource matvalueResource = salePriceResource.getMatvalueResource();
        Matvalue matvalue = this.matvalueConverter.convert(matvalueResource);

        SalePrice salePrice = new SalePrice();
        salePrice.setID(ID);
        salePrice.setMatvalue(matvalue);
        salePrice.setDepartment(department);
        salePrice.setCurrency(currency);
        salePrice.setDate(date);
        salePrice.setPrice(price);
        salePrice.setQuantity(quantity);
        return salePrice;
    }
}
