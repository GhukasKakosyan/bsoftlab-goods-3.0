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
@Entity(name = "Unitofmsr")
@Table(name = "refunitsofmsrs", schema = "materials")
public class Unitofmsr implements Comparable<Unitofmsr>, Serializable {

    @Id
    @Column(name = "Code", unique = true, nullable = false, updatable = false)
    private String code = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "ShortName", nullable = false)
    private String shortName = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "LongName", nullable = false)
    private String longName = null;

    public Unitofmsr() {}

    public String getCode() {
        return this.code;
    }
    public String getShortName() {
        return this.shortName;
    }
    public String getLongName() {
        return this.longName;
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

    @Override
    public int compareTo(Unitofmsr unitofmsr) {
        return this.getCode().compareTo(unitofmsr.getCode());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !this.getClass().equals(object.getClass())) {
            return false;
        }
        Unitofmsr unitofmsr = (Unitofmsr)object;
        return this.code.equals(unitofmsr.getCode()) &&
                this.shortName.equals(unitofmsr.getShortName()) &&
                this.longName.equals(unitofmsr.getLongName());
    }

    @Override
    public String toString() {
        return "[" +
                this.code + ", " +
                this.shortName + ", " +
                this.longName + "]";
    }
}