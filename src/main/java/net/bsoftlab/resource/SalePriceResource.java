package net.bsoftlab.resource;

import org.springframework.hateoas.ResourceSupport;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SalePriceResource extends ResourceSupport
        implements Comparable<SalePriceResource> {

    private String code = null;
    private MatvalueResource matvalueResource = null;
    private DepartmentResource departmentResource = null;
    private CurrencyResource currencyResource = null;
    private String date = null;
    private String price = null;
    private String quantity = null;
    private boolean selected = false;

    public SalePriceResource() {}

    public String getCode() {
        return this.code;
    }
    public MatvalueResource getMatvalueResource() {
        return this.matvalueResource;
    }
    public DepartmentResource getDepartmentResource() {
        return this.departmentResource;
    }
    public CurrencyResource getCurrencyResource() {
        return this.currencyResource;
    }
    public String getDate() {
        return this.date;
    }
    public String getPrice() {
        return this.price;
    }
    public String getQuantity() {
        return this.quantity;
    }
    public boolean getSelected() { return this.selected; }

    public void setCode(String code) {
        this.code = code;
    }
    public void setMatvalueResource(
            MatvalueResource matvalueResource) {
        this.matvalueResource = matvalueResource;
    }
    public void setDepartmentResource(
            DepartmentResource departmentResource) {
        this.departmentResource = departmentResource;
    }
    public void setCurrencyResource(
            CurrencyResource currencyResource) {
        this.currencyResource = currencyResource;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    public void setSelected(boolean selected) { this.selected = selected; }

    @Override
    public int compareTo(SalePriceResource salePriceResource) {
        String compared = this.departmentResource.getCode() +
                this.matvalueResource.getCode() + this.getDate();
        String comparable = salePriceResource.getDepartmentResource().getCode() +
                salePriceResource.getMatvalueResource().getCode() +
                salePriceResource.getDate();
        return compared.compareTo(comparable);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !this.getClass().equals(object.getClass())) {
            return false;
        }
        SalePriceResource salePriceResource = (SalePriceResource) object;
        if (!this.getLinks().equals(salePriceResource.getLinks())) {
            return false;
        }
        return this.code.equals(salePriceResource.getCode()) &&
                this.matvalueResource.equals(salePriceResource.getMatvalueResource()) &&
                this.departmentResource.equals(salePriceResource.getDepartmentResource()) &&
                this.currencyResource.equals(salePriceResource.getCurrencyResource()) &&
                this.date.equals(salePriceResource.getDate()) &&
                this.price.equals(salePriceResource.getPrice()) &&
                this.quantity.equals(salePriceResource.getQuantity());
    }

    public void setInitialValues() {
        Format format = new SimpleDateFormat("dd.MM.yyyy");
        String dateText = format.format(new Date());

        this.code = null;
        this.matvalueResource = new MatvalueResource();
        this.matvalueResource.setInitialValues();
        this.departmentResource = new DepartmentResource();
        this.departmentResource.setInitialValues();
        this.currencyResource = new CurrencyResource();
        this.currencyResource.setInitialValues();
        this.date = dateText;
        this.price = "0.00";
        this.quantity = "0.000";
    }

    @Override
    public String toString() {
        return this.code + ", " +
                this.matvalueResource + "; " +
                this.departmentResource + "; " +
                this.currencyResource + "; " +
                this.date + ", " +
                this.price + ", " +
                this.quantity;
    }
}
