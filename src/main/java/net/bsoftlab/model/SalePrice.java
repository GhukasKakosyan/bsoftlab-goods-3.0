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
@Entity(name = "SalePrice")
@Table(name = "refpricesofmatvalues", schema = "materials",
        uniqueConstraints = {@UniqueConstraint(name = "UK_SalePrice",
                columnNames = {"DepartmentCode", "MatvalueCode", "Date"})})
public class SalePrice implements Comparable<SalePrice>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Integer ID = null;

    @ManyToOne(targetEntity = Matvalue.class,
            fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "MatvalueCode", nullable = false)
    private Matvalue matvalue = null;

    @ManyToOne(targetEntity = Department.class,
            fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "DepartmentCode", nullable = false)
    private Department department = null;

    @ManyToOne(targetEntity = Currency.class,
            fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "CurrencyCode", nullable = false)
    private Currency currency = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "Date", nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date date = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "Price", nullable = false)
    private Double price = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "Quantity", nullable = false)
    private Double quantity = null;

    public SalePrice() {}

    public Integer getID() {
        return this.ID;
    }
    public Matvalue getMatvalue() {
        return this.matvalue;
    }
    public Department getDepartment() {
        return this.department;
    }
    public Currency getCurrency() {
        return this.currency;
    }
    public Date getDate() {
        return this.date;
    }
    public Double getPrice() {
        return this.price;
    }
    public Double getQuantity() {
        return this.quantity;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }
    public void setMatvalue(Matvalue matvalue) {
        this.matvalue = matvalue;
    }
    public void setDepartment(Department department) {
        this.department = department;
    }
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    @Override
    public int compareTo(SalePrice salePrice) {
        String compared = this.department.getCode() +
                this.matvalue.getCode() + this.getDate();
        String comparable = salePrice.getDepartment().getCode() +
                salePrice.getMatvalue().getCode() + salePrice.getDate();
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
        SalePrice salePrice = (SalePrice)object;
        return this.ID.equals(salePrice.getID()) &&
                this.matvalue.equals(salePrice.getMatvalue()) &&
                this.department.equals(salePrice.getDepartment()) &&
                this.currency.equals(salePrice.getCurrency()) &&
                this.date.equals(salePrice.getDate()) &&
                this.price.equals(salePrice.getPrice()) &&
                this.quantity.equals(salePrice.getQuantity());
    }

    @Override
    public String toString() {
        return "[" +
                this.ID + ", " +
                this.matvalue + ", " +
                this.department + ", " +
                this.currency + ", " +
                this.date + ", " +
                this.price + ", " +
                this.quantity + "]";
    }
}