package net.bsoftlab.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import java.io.Serializable;
import java.lang.Comparable;

@Access(value = AccessType.FIELD)
@Entity(name = "Permission")
@Table(name = "refpermissions", schema = "materials", uniqueConstraints = {
        @UniqueConstraint(name = "UK_Permission", columnNames = {"Name"})})
public class Permission implements Comparable<Permission>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Integer ID = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "Name", unique = true, nullable = false)
    private String name = null;

    public Permission() {}

    public Integer getID() {
        return this.ID;
    }
    public String getName() {
        return this.name;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Permission permission) {
        return this.getName().compareTo(permission.getName());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !this.getClass().equals(object.getClass())) {
            return false;
        }
        Permission permission = (Permission) object;
        return this.ID.equals(permission.getID()) &&
                this.name.equals(permission.getName());
    }

    @Override
    public String toString() {
        return this.ID + ", " + this.name;
    }
}