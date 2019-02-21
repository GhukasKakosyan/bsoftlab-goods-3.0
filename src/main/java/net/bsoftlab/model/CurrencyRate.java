package net.bsoftlab.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import java.io.Serializable;
import java.lang.Comparable;
import java.util.Date;

@Access(value = AccessType.FIELD)
@Entity(name = "CurrencyRate")
@Table(name = "refcurrenciesrates", schema = "materials",
        uniqueConstraints = {@UniqueConstraint(name = "UK_CurrencyRate",
                columnNames = {"CurrencyCode", "Date"})})
public class CurrencyRate implements Comparable<CurrencyRate>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Integer ID = null;

    @ManyToOne(targetEntity = Currency.class, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "CurrencyCode", nullable = false)
    private Currency currency = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "Date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "Rate", nullable = false)
    private Double rate = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "Quantity", nullable = false)
    private Double quantity = null;

    public CurrencyRate() {}

    public Integer getID() {
        return this.ID;
    }
    public Currency getCurrency() {
        return this.currency;
    }
    public Date getDate() {
        return this.date;
    }
    public Double getRate() {
        return this.rate;
    }
    public Double getQuantity() {
        return this.quantity;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setRate(Double rate) {
        this.rate = rate;
    }
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    @Override
    public int compareTo(CurrencyRate currencyRate) {
        String compared = this.getCurrency().getCode() + this.getDate();
        String comparable = currencyRate.getCurrency().getCode() + currencyRate.getDate();
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
        CurrencyRate currencyRate = (CurrencyRate) object;
        return this.ID.equals(currencyRate.getID()) &&
                this.currency.equals(currencyRate.getCurrency()) &&
                this.date.equals(currencyRate.getDate()) &&
                this.rate.equals(currencyRate.getRate()) &&
                this.quantity.equals(currencyRate.getQuantity());
    }

    @Override
    public String toString() {
        return this.ID + ", " +
                this.currency + ", " +
                this.date + ", " +
                this.rate + ", " +
                this.quantity;
    }
}