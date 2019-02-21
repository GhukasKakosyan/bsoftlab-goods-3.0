package net.bsoftlab.resource;

import org.springframework.hateoas.ResourceSupport;

public class WorkmanResource extends ResourceSupport
        implements Comparable<WorkmanResource> {

    private String code = null;
    private String name = null;
    private String password = null;
    private String firstName = null;
    private String lastName = null;
    private String phones = null;
    private AddressResource addressResource = null;
    private RoleResource roleResource = null;
    private boolean selected = false;

    public WorkmanResource() {}

    public String getCode() {
        return this.code;
    }
    public String getName() {
        return this.name;
    }
    public String getPassword() {
        return this.password;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {
        return this.lastName;
    }
    public String getPhones() {
        return this.phones;
    }
    public AddressResource getAddressResource() {
        return this.addressResource;
    }
    public RoleResource getRoleResource() { return this.roleResource; }
    public boolean getSelected() { return this.selected; }

    public void setCode(String code) {
        this.code = code;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setPhones(String phones) {
        this.phones = phones;
    }
    public void setAddressResource(
            AddressResource addressResource) {
        this.addressResource = addressResource;
    }
    public void setRoleResource(
            RoleResource roleResource) {
        this.roleResource = roleResource;
    }
    public void setSelected(boolean selected) { this.selected = selected; }

    @Override
    public int compareTo(WorkmanResource workmanResource) {
        return this.getName().compareTo(workmanResource.getName());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !this.getClass().equals(object.getClass())) {
            return false;
        }
        WorkmanResource workmanResource = (WorkmanResource) object;
        if (!this.getLinks().equals(workmanResource.getLinks())) {
            return false;
        }
        return this.code.equals(workmanResource.getCode()) &&
                this.name.equals(workmanResource.getName()) &&
                this.password.equals(workmanResource.getPassword()) &&
                this.firstName.equals(workmanResource.getFirstName()) &&
                this.lastName.equals(workmanResource.getLastName()) &&
                this.phones.equals(workmanResource.getPhones()) &&
                this.addressResource.equals(workmanResource.getAddressResource());
    }

    public void setInitialValues() {
        this.code = null;
        this.name = "";
        this.password = "";
        this.firstName = "";
        this.lastName = "";
        this.phones = "";
        this.addressResource = new AddressResource();
        this.addressResource.setInitialValues();
        this.roleResource = new RoleResource();
        this.roleResource.setInitialValues();
    }

    @Override
    public String toString() {
        return this.code + ", " +
                this.name + ", " +
                this.firstName + ", " +
                this.lastName + ", " +
                this.phones + ", " +
                this.addressResource + ", " +
                this.roleResource;
    }
}