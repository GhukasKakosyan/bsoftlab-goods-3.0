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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import java.io.Serializable;
import java.lang.Comparable;
import java.util.LinkedHashSet;
import java.util.Set;

@Access(value = AccessType.FIELD)
@Entity(name = "Role")
@Table(name = "refroles", schema = "materials", uniqueConstraints = {
        @UniqueConstraint(name = "UK_Role", columnNames = {"Name"})})
public class Role implements Comparable<Role>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Integer ID = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "Name", unique = true, nullable = false)
    private String name = null;

    @ManyToMany(targetEntity = Permission.class, fetch = FetchType.EAGER)
    @JoinTable(name = "refrolespermissions", schema = "materials",
            joinColumns = @JoinColumn(name = "RoleID"),
            inverseJoinColumns = @JoinColumn(name = "PermissionID"))
    private Set<Permission> permissions = new LinkedHashSet<>();

    @ManyToMany(targetEntity = Workman.class, fetch = FetchType.EAGER)
    @JoinTable(name = "refworkmansroles", schema = "materials",
            joinColumns = @JoinColumn(name = "RoleID"),
            inverseJoinColumns = @JoinColumn(name = "WorkmanID"))
    private Set<Workman> workmans = new LinkedHashSet<>();

    public Role() {}

    public Integer getID() {
        return this.ID;
    }
    public String getName() {
        return this.name;
    }
    public Set<Permission> getPermissions() {
        return this.permissions;
    }
    public Set<Workman> getWorkmans() { return this.workmans; }

    public void setID(Integer ID) {
        this.ID = ID;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
    public void setWorkmans(Set<Workman> workmans) { this.workmans = workmans; }

    @Override
    public int compareTo(Role role) {
        return this.getName().compareTo(role.getName());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !this.getClass().equals(object.getClass())) {
            return false;
        }
        Role role = (Role) object;
        return this.ID.equals(role.getID()) &&
                this.name.equals(role.getName());
    }

    @Override
    public String toString() {
        return this.ID + ", " + this.name;
    }
}
