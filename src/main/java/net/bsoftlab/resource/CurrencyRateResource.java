package net.bsoftlab.resource;

import org.springframework.hateoas.ResourceSupport;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrencyRateResource extends ResourceSupport
        implements Comparable<CurrencyRateResource> {

    private String code = null;
    private CurrencyResource currencyResource = null;
    private String date = null;
    private String rate = null;
    private String quantity = null;
    private boolean selected = false;

    public CurrencyRateResource() {}

    public String getCode() {
        return this.code;
    }
    public CurrencyResource getCurrencyResource() {
        return this.currencyResource;
    }
    public String getDate() {
        return this.date;
    }
    public String getRate() {
        return this.rate;
    }
    public String getQuantity() {
        return this.quantity;
    }
    public boolean getSelected() { return this.selected; }

    public void setCode(String code) {
        this.code = code;
    }
    public void setCurrencyResource(CurrencyResource currencyResource) {
        this.currencyResource = currencyResource;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setRate(String rate) {
        this.rate = rate;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    public void setSelected(boolean selected) { this.selected = selected; }

    @Override
    public int compareTo(CurrencyRateResource currencyRateResource) {
        String compared = this.getCurrencyResource().getCode() + this.getDate();
        String comparable = currencyRateResource.getCurrencyResource().getCode() +
                currencyRateResource.getDate();
        return compared.compareTo(comparable);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !object.getClass().equals(this.getClass())) {
            return false;
        }
        CurrencyRateResource currencyRateResource = (CurrencyRateResource) object;
        if (!this.getLinks().equals(currencyRateResource.getLinks())) {
            return false;
        }
        return this.code.equals(currencyRateResource.getCode()) &&
                this.currencyResource.equals(currencyRateResource.getCurrencyResource()) &&
                this.date.equals(currencyRateResource.getDate()) &&
                this.rate.equals(currencyRateResource.getRate()) &&
                this.quantity.equals(currencyRateResource.getQuantity());
    }

    public void setInitialValues() {
        Format format = new SimpleDateFormat("dd.MM.yyyy");
        String dateText = format.format(new Date());

        this.code = null;
        this.currencyResource = new CurrencyResource();
        this.currencyResource.setInitialValues();
        this.date = dateText;
        this.rate = "0.00";
        this.quantity = "0.000";
    }

    @Override
    public String toString() {
        return "[" +
                this.code + ", " +
                this.currencyResource + ", " +
                this.date + ", " +
                this.rate + ", " +
                this.quantity + "]";
    }
}