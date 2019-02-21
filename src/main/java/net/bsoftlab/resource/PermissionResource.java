package net.bsoftlab.resource;

import org.springframework.hateoas.ResourceSupport;

public class PermissionResource extends ResourceSupport
        implements Comparable<PermissionResource> {

    private String code = null;
    private String name = null;
    private boolean selected = false;

    public PermissionResource () {}

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
    public int compareTo(PermissionResource permissionResource) {
        return this.getName().compareTo(permissionResource.getName());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !this.getClass().equals(object.getClass())) {
            return false;
        }
        PermissionResource permissionResource = (PermissionResource) object;
        if (!this.getLinks().equals(permissionResource.getLinks())) {
            return false;
        }
        return this.code.equals(permissionResource.getCode()) &&
                this.name.equals(permissionResource.getName());
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
