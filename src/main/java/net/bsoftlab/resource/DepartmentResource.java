package net.bsoftlab.resource;

import org.springframework.hateoas.ResourceSupport;

public class DepartmentResource extends ResourceSupport
        implements Comparable<DepartmentResource> {

    private String code = null;
    private String name = null;
    private AddressResource addressResource = null;
    private String phones = null;
    private String faxes = null;
    private String webSite = null;
    private String emailAddress = null;
    private String additionalInformation = null;
    private boolean selected = false;

    public DepartmentResource() {}

    public String getCode() {
        return this.code;
    }
    public String getName() {
        return this.name;
    }
    public AddressResource getAddressResource() {
        return this.addressResource;
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
    public boolean getSelected() { return this.selected; }

    public void setCode(String code) {
        this.code = code;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAddressResource(AddressResource addressResource) {
        this.addressResource = addressResource;
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
    public void setSelected(boolean selected) { this.selected = selected; }

    @Override
    public int compareTo(DepartmentResource departmentResource) {
        return this.getCode().compareTo(departmentResource.getCode());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !this.getClass().equals(object.getClass())) {
            return false;
        }
        DepartmentResource departmentResource = (DepartmentResource)object;
        if (!this.getLinks().equals(departmentResource.getLinks())) {
            return false;
        }
        return this.code.equals(departmentResource.getCode()) &&
                this.name.equals(departmentResource.getName()) &&
                this.addressResource.equals(departmentResource.getAddressResource()) &&
                this.phones.equals(departmentResource.getPhones()) &&
                this.faxes.equals(departmentResource.getFaxes()) &&
                this.webSite.equals(departmentResource.getWebSite()) &&
                this.emailAddress.equals(departmentResource.getEmailAddress()) &&
                this.additionalInformation.equals(departmentResource.getAdditionalInformation());
    }

    public void setInitialValues() {
        this.code = "";
        this.name = "";
        this.addressResource = new AddressResource();
        this.addressResource.setInitialValues();
        this.phones = "";
        this.faxes = "";
        this.webSite = "";
        this.emailAddress = "";
        this.additionalInformation = "";
    }

    @Override
    public String toString() {
        return "[" +
                this.code + ", " +
                this.name + ", " +
                this.addressResource + ", " +
                this.phones + ", " +
                this.faxes + ", " +
                this.webSite + ", " +
                this.emailAddress + ", " +
                this.additionalInformation + "]";
    }
}
