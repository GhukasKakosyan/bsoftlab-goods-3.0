package net.bsoftlab.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.lang.Comparable;

@Access(value = AccessType.FIELD)
@Entity(name = "Department")
@Table(name = "refdepartments", schema = "materials")
public class Department implements Comparable<Department>, Serializable {

    @Id
    @Column(name = "Code", unique = true, nullable = false, updatable = false)
    private String code = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "Name", nullable = false)
    private String name = null;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street",
                    column = @Column(name = "Street", nullable = false)),
            @AttributeOverride(name = "pincode",
                    column = @Column(name = "Pincode", nullable = false)),
            @AttributeOverride(name = "city",
                    column = @Column(name = "City", nullable = false)),
            @AttributeOverride(name = "state",
                    column = @Column(name = "State", nullable = false)),
            @AttributeOverride(name = "country",
                    column = @Column(name = "Country", nullable = false))
    })
    private Address address = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "Phones", nullable = false)
    private String phones = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "Faxes", nullable = false)
    private String faxes = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "WebSite", nullable = false)
    private String webSite = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "EmailAddress", nullable = false)
    private String emailAddress = null;

    @Basic(fetch = FetchType.EAGER)
    @Column(name = "AdditionalInformation")
    private String additionalInformation = null;

    public Department() {}

    public String getCode() {
        return this.code;
    }
    public String getName() {
        return this.name;
    }
    public Address getAddress() {
        return this.address;
    }
    public String getPhones() {
        return this.phones;
    }
    public String getFaxes() {
        return this.faxes;
    }
    public String getWebSite() {
        return this.webSite;
    }
    public String getEmailAddress() {
        return this.emailAddress;
    }
    public String getAdditionalInformation() {
        return this.additionalInformation;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
    public void setPhones(String phones) {
        this.phones = phones;
    }
    public void setFaxes(String faxes) {
        this.faxes = faxes;
    }
    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    @Override
    public int compareTo(Department department) {
        return this.getCode().compareTo(department.getCode());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !this.getClass().equals(object.getClass())) {
            return false;
        }
        Department department = (Department) object;
        return this.code.equals(department.getCode()) &&
                this.name.equals(department.getName()) &&
                this.address.equals(department.getAddress()) &&
                this.phones.equals(department.getPhones()) &&
                this.faxes.equals(department.getFaxes()) &&
                this.webSite.equals(department.getWebSite()) &&
                this.emailAddress.equals(department.getEmailAddress()) &&
                this.additionalInformation.equals(department.getAdditionalInformation());
    }

    @Override
    public String toString() {
        return "[" +
                this.code + ", " +
                this.name + ", " +
                this.address + ", " +
                this.phones + ", " +
                this.faxes + ", " +
                this.webSite + ", " +
                this.emailAddress + ", " +
                this.additionalInformation + "]";
    }
}