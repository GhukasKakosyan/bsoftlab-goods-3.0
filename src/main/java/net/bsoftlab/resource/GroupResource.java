package net.bsoftlab.resource;

import org.springframework.hateoas.ResourceSupport;

public class GroupResource extends ResourceSupport
        implements Comparable<GroupResource> {

    private String code = null;
    private String name = null;
    private String enhancedName = null;
    private boolean selected = false;

    public GroupResource() {}

    public String getCode() {
        return this.code;
    }
    public String getName() {
        return this.name;
    }
    public String getEnhancedName() {
        return this.enhancedName;
    }
    public boolean getSelected() { return this.selected; }

    public void setCode(String code) {
        this.code = code;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEnhancedName(String enhancedName) {
        this.enhancedName = enhancedName;
    }
    public void setSelected(boolean selected) { this.selected = selected; }

    @Override
    public int compareTo(GroupResource groupResource) {
        return this.getCode().compareTo(groupResource.getCode());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !this.getClass().equals(object.getClass())) {
            return false;
        }
        GroupResource groupResource = (GroupResource) object;
        if (!this.getLinks().equals(groupResource.getLinks())) {
            return false;
        }
        return this.code.equals(groupResource.getCode()) &&
                this.name.equals(groupResource.getName()) &&
                this.enhancedName.equals(groupResource.getEnhancedName());
    }

    public void setInitialValues() {
        this.code = "";
        this.name = "";
        this.enhancedName = "";
    }

    @Override
    public String toString() {
        return "[" +
                this.code + ", " +
                this.name + ", " +
                this.enhancedName + "]";
    }
}