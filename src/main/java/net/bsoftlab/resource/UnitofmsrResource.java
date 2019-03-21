package net.bsoftlab.resource;

import org.springframework.hateoas.ResourceSupport;

public class UnitofmsrResource extends ResourceSupport
        implements Comparable<UnitofmsrResource> {

    private String code = null;
    private String shortName = null;
    private String longName = null;
    private boolean selected = false;

    public UnitofmsrResource() {}

    public String getCode() {
        return this.code;
    }
    public String getShortName() {
        return this.shortName;
    }
    public String getLongName() {
        return this.longName;
    }
    public boolean getSelected() { return this.selected; }

    public void setCode(String code) {
        this.code = code;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    public void setLongName(String longName) {
        this.longName = longName;
    }
    public void setSelected(boolean selected) { this.selected = selected; }

    @Override
    public int compareTo(UnitofmsrResource unitofmsrResource) {
        return this.getCode().compareTo(unitofmsrResource.getCode());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !this.getClass().equals(object.getClass())) {
            return false;
        }
        UnitofmsrResource unitofmsrResource = (UnitofmsrResource) object;
        if (!this.getLinks().equals(unitofmsrResource.getLinks())) {
            return false;
        }
        return this.code.equals(unitofmsrResource.getCode()) &&
                this.shortName.equals(unitofmsrResource.getShortName()) &&
                this.longName.equals(unitofmsrResource.getLongName());
    }

    public void setInitialValues() {
        this.code = "";
        this.shortName = "";
        this.longName = "";
    }

    @Override
    public String toString() {
        return "[" +
                this.code + ", " +
                this.shortName + ", " +
                this.longName + "]";
    }
}
