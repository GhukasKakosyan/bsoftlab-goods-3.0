package net.bsoftlab.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.lang.Comparable;

@Access(value = AccessType.FIELD)
@Entity(name = "GroupProduct")
@Table(name = "refgroups", schema = "materials")
public class Group implements Comparable<Group>, Serializable {

    @Id
    @Column(name = "Code", unique = true, nullable = false, updatable = false)
    private String code = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "Name", nullable = false)
    private String name = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "EnhancedName", nullable = false)
    private String enhancedName = null;

    public Group() {}

    public String getCode() {
        return this.code;
    }
    public String getName() {
        return this.name;
    }
    public String getEnhancedName() {
        return this.enhancedName;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEnhancedName(String enhancedName) {
        this.enhancedName = enhancedName;
    }

    @Override
    public int compareTo(Group group) {
        return this.getCode().compareTo(group.getCode());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !this.getClass().equals(object.getClass())) {
            return false;
        }
        Group group = (Group) object;
        return this.code.equals(group.getCode()) &&
                this.name.equals(group.getName()) &&
                this.enhancedName.equals(group.getEnhancedName());
    }

    @Override
    public String toString() {
        return "[" +
                this.code + ", " +
                this.name + ", " +
                this.enhancedName + "]";
    }
}