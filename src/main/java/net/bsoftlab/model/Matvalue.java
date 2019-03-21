package net.bsoftlab.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.io.Serializable;
import java.lang.Comparable;
import java.util.LinkedHashSet;
import java.util.Set;

@Access(value = AccessType.FIELD)
@Entity(name = "Matvalue")
@Table(name = "refmatvalues", schema = "materials")
public class Matvalue implements Comparable<Matvalue>, Serializable {

    @Id
    @Column(name = "Code", unique = true, nullable = false, updatable = false)
    private String code = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "Name", nullable = false)
    private String name = null;

    @ManyToOne(targetEntity = Unitofmsr.class,
            fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "UnitofmsrCode", nullable = false)
    private Unitofmsr unitofmsr = null;

    @ManyToOne(targetEntity = Group.class,
            fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "GroupCode", nullable = false)
    private Group group = null;

    @OneToMany(targetEntity = SalePrice.class, cascade = {CascadeType.ALL},
            fetch = FetchType.EAGER, mappedBy = "matvalue", orphanRemoval = true)
    private Set<SalePrice> salePriceSet = new LinkedHashSet<>();

    public Matvalue() {}

    public String getCode() {
        return this.code;
    }
    public String getName() {
        return this.name;
    }
    public Unitofmsr getUnitofmsr() {
        return this.unitofmsr;
    }
    public Group getGroup() {
        return this.group;
    }
    public Set<SalePrice> getSalePriceSet() {
        return this.salePriceSet;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setUnitofmsr(Unitofmsr unitofmsr) {
        this.unitofmsr = unitofmsr;
    }
    public void setGroup(Group group) {
        this.group = group;
    }
    public void setSalePriceSet(Set<SalePrice> salePriceSet) {
        this.salePriceSet = salePriceSet;
    }

    @Override
    public int compareTo(Matvalue matvalue) {
        return this.getCode().compareTo(matvalue.getCode());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !this.getClass().equals(object.getClass())) {
            return false;
        }
        Matvalue matvalue = (Matvalue) object;
        return this.code.equals(matvalue.getCode()) &&
                this.name.equals(matvalue.getName()) &&
                this.unitofmsr.equals(matvalue.getUnitofmsr()) &&
                this.group.equals(matvalue.getGroup());
    }

    @Override
    public String toString() {
        return "[" +
                this.code + ", " +
                this.name + ", " +
                this.unitofmsr + ", " +
                this.group + "]";
    }
}