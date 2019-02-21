package net.bsoftlab.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;

import java.io.Serializable;
import java.lang.Comparable;

@Access(value = AccessType.FIELD)
@Embeddable
public class Address implements Comparable<Address>, Serializable {

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "Street", nullable = false)
    private String street = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "Pincode", nullable = false)
    private String pincode = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "City", nullable = false)
    private String city = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "State", nullable = false)
    private String state = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "Country", nullable = false)
    private String country = null;

    public Address() {}

    public String getStreet() {
        return this.street;
    }
    public String getPincode() {
        return this.pincode;
    }
    public String getCity() {
        return this.city;
    }
    public String getState() {
        return this.state;
    }
    public String getCountry() {
        return this.country;
    }

    public void setStreet(String street) {
        this.street = street;
    }
    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setState(String state) {
        this.state = state;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public int compareTo(Address address) {
        return this.toString().compareTo(address.toString());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !this.getClass().equals(object.getClass())) {
            return false;
        }
        Address address = (Address) object;
        return this.street.equals(address.getStreet()) &&
                this.pincode.equals(address.getPincode()) &&
                this.city.equals(address.getCity()) &&
                this.state.equals(address.getState()) &&
                this.country.equals(address.getCountry());
    }

    @Override
    public String toString() {
        return this.country + ", " +
                this.state + ", " +
                this.city + ", " +
                this.pincode + ", " +
                this.street;
    }
}