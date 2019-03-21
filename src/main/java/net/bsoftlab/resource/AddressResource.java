package net.bsoftlab.resource;

public class AddressResource implements Comparable<AddressResource> {

    private String street = null;
    private String pincode = null;
    private String city = null;
    private String state = null;
    private String country = null;

    public AddressResource() {}

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
    public int compareTo(AddressResource addressResource) {
        return this.toString().compareTo(addressResource.toString());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !this.getClass().equals(object.getClass())) {
            return false;
        }
        AddressResource addressResource = (AddressResource) object;
        return this.street.equals(addressResource.getStreet()) &&
                this.pincode.equals(addressResource.getPincode()) &&
                this.city.equals(addressResource.getCity()) &&
                this.state.equals(addressResource.getState()) &&
                this.country.equals(addressResource.getCountry());
    }

    public void setInitialValues() {
        this.street = "";
        this.pincode = "";
        this.city = "";
        this.state = "";
        this.country = "";
    }

    @Override
    public String toString() {
        return "[" +
                this.country + ", " +
                this.state + ", " +
                this.city + ", " +
                this.pincode + ", " +
                this.street + "]";
    }
}
