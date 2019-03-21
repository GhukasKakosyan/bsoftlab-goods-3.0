package net.bsoftlab.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.lang.Comparable;

@Access(value = AccessType.FIELD)
@Entity(name = "Currency")
@Table(name = "refcurrencies", schema = "materials")
public class Currency implements Comparable<Currency>, Serializable {

    @Id
    @Column(name = "Code", unique = true, nullable = false, updatable = false)
    private String code = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "ShortName", nullable = false)
    private String shortName = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "LongName", nullable = false)
    private String longName = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "Country", nullable = false)
    private String country = null;

    @Basic(fetch = FetchType.EAGER)
    @Column(name = "AdditionalInformation")
    private String additionalInformation = null;

    public Currency() {}

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

    @Override
    public int compareTo(Currency currency) {
        return this.getCode().compareTo(currency.getCode());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !this.getClass().equals(object.getClass())) {
            return false;
        }
        Currency currency = (Currency) object;
        return this.code.equals(currency.getCode()) &&
                this.shortName.equals(currency.getShortName()) &&
                this.longName.equals(currency.getLongName()) &&
                this.country.equals(currency.getCountry()) &&
                this.additionalInformation.equals(currency.getAdditionalInformation());
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