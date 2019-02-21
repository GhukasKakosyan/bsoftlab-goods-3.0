package net.bsoftlab.resource;

import org.springframework.hateoas.ResourceSupport;

public class RoleResource extends ResourceSupport
        implements Comparable<RoleResource> {

    private String code = null;
    private String name = null;
    private boolean selected = false;

    public RoleResource() {}

    public String getCode() {
        return this.code;
    }
    public String getName() {
        return this.name;
    }
    public boolean getSelected() {
        return this.selected;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int compareTo(RoleResource roleResource) {
        return this.getName().compareTo(roleResource.getName());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !this.getClass().equals(object.getClass())) {
            return false;
        }
        RoleResource roleResource = (RoleResource) object;
        if (!this.getLinks().equals(roleResource.getLinks())) {
            return false;
        }
        return this.code.equals(roleResource.getCode()) &&
                this.name.equals(roleResource.getName());
    }

    public void setInitialValues() {
        this.code = "";
        this.name = "";
    }

    @Override
    public String toString() {
        return this.code + ", " + this.name;
    }
}