package net.bsoftlab.resource;

import org.springframework.hateoas.ResourceSupport;

public class CurrencyResource extends ResourceSupport
        implements Comparable<CurrencyResource> {

    private String code = null;
    private String shortName = null;
    private String longName = null;
    private String country = null;
    private String additionalInformation = null;
    private boolean selected = false;

    public CurrencyResource() {}

    public String getCode() {
        return this.code;
    }
    public String getShortName() {
        return this.shortName;
    }
    public String getLongName() {
        return this.longName;
    }
    public String getCountry() {
        return this.country;
    }
    public String getAdditionalInformation() {
        return this.additionalInformation;
    }
    public boolean getSelected() { return this.selected; }

    public void setCode(String code) {
        this.code = code;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    public void setLongName(String longName) {
        this.longName = longName;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int compareTo(CurrencyResource currencyResource) {
        return this.getCode().compareTo(currencyResource.getCode());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !this.getClass().equals(object.getClass())) {
            return false;
        }
        CurrencyResource currencyResource = (CurrencyResource) object;
        if (!this.getLinks().equals(currencyResource.getLinks())) {
            return false;
        }
        return this.code.equals(currencyResource.getCode()) &&
                this.shortName.equals(currencyResource.getShortName()) &&
                this.longName.equals(currencyResource.getLongName()) &&
                this.country.equals(currencyResource.getCountry()) &&
                this.additionalInformation.equals(currencyResource.getAdditionalInformation());
    }

    public void setInitialValues() {
        this.code = "";
        this.shortName = "";
        this.longName = "";
        this.country = "";
        this.additionalInformation = "";
    }

    @Override
    public String toString() {
        return "[" +
                this.code + ", " +
                this.shortName + ", " +
                this.longName + ", " +
                this.country + ", " +
                this.additionalInformation + "]";
    }
}
