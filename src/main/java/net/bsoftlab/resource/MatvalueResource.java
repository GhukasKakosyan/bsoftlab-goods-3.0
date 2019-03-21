package net.bsoftlab.resource;

import org.springframework.hateoas.ResourceSupport;

public class MatvalueResource extends ResourceSupport
        implements Comparable<MatvalueResource> {

    private String code = null;
    private String name = null;
    private UnitofmsrResource unitofmsrResource = null;
    private GroupResource groupResource = null;
    private boolean selected = false;

    public MatvalueResource() {}

    public String getCode() {
        return this.code;
    }
    public String getName() {
        return this.name;
    }
    public UnitofmsrResource getUnitofmsrResource() {
        return this.unitofmsrResource;
    }
    public GroupResource getGroupResource() {
        return this.groupResource;
    }
    public boolean getSelected() { return this.selected; }

    public void setCode(String code) {
        this.code = code;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setUnitofmsrResource(UnitofmsrResource unitofmsrResource) {
        this.unitofmsrResource = unitofmsrResource;
    }
    public void setGroupResource(GroupResource groupResource) {
        this.groupResource = groupResource;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int compareTo(MatvalueResource matvalueResource) {
        return this.getCode().compareTo(matvalueResource.getCode());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !this.getClass().equals(object.getClass())) {
            return false;
        }
        MatvalueResource matvalueResource = (MatvalueResource) object;
        if (!this.getLinks().equals(matvalueResource.getLinks())) {
            return false;
        }
        return this.code.equals(matvalueResource.getCode()) &&
                this.name.equals(matvalueResource.getName()) &&
                this.unitofmsrResource.equals(matvalueResource.getUnitofmsrResource()) &&
                this.groupResource.equals(matvalueResource.getGroupResource());
    }

    public void setInitialValues() {
        this.code = "";
        this.name = "";
        this.unitofmsrResource = new UnitofmsrResource();
        this.unitofmsrResource.setInitialValues();
        this.groupResource = new GroupResource();
        this.groupResource.setInitialValues();
    }

    @Override
    public String toString() {
        return "[" +
                this.code + ", " +
                this.name + ", " +
                this.unitofmsrResource + "; " +
                this.groupResource + "]";
    }
}