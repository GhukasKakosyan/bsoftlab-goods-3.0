package net.bsoftlab.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
@Entity(name = "Workman")
@Table(name = "refworkmans", schema = "materials", uniqueConstraints = {
        @UniqueConstraint(name = "UK_Workman", columnNames = {"Name"})})
public class Workman implements Comparable<Workman>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private Integer ID = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "Name", unique = true, nullable = false)
    private String name = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "Password", nullable = false)
    private String password = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "FirstName", nullable = false)
    private String firstName = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "LastName", nullable = false)
    private String lastName = null;

    @Basic(fetch = FetchType.EAGER, optional = false)
    @Column(name = "Phones", nullable = false)
    private String phones = null;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street",
                    column = @Column(name = "Street", nullable = false)),
            @AttributeOverride(name = "pincode",
                    column = @Column(name = "Pincode", nullable = false)),
            @AttributeOverride(name = "city",
                    column = @Column(name = "City", nullable = false)),
            @AttributeOverride(name = "state",
                    column = @Column(name = "State", nullable = false)),
            @AttributeOverride(name = "country",
                    column = @Column(name = "Country", nullable = false))
    })
    private Address address = null;

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "refworkmansroles", schema = "materials",
            joinColumns = @JoinColumn(name = "WorkmanID"),
            inverseJoinColumns = @JoinColumn(name = "RoleID"))
    private Set<Role> roles = new LinkedHashSet<>();

    public Workman() {}

    public Integer getID() {
        return this.ID;
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
    public Address getAddress() {
        return this.address;
    }
    public Set<Role> getRoles() { return this.roles; }

    public void setID(Integer ID) {
        this.ID = ID;
    }
    public void setName(String email) {
        this.name = email;
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
    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public int compareTo(Workman workman) {
        return this.getName().compareTo(workman.getName());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !this.getClass().equals(object.getClass())) {
            return false;
        }
        Workman workman = (Workman)object;
        return this.ID.equals(workman.getID()) &&
                this.name.equals(workman.getName()) &&
                this.password.equals(workman.getPassword()) &&
                this.firstName.equals(workman.getFirstName()) &&
                this.lastName.equals(workman.getLastName()) &&
                this.phones.equals(workman.getPhones()) &&
                this.address.equals(workman.getAddress());
    }

    @Override
    public String toString() {
        return this.ID + ", " +
                this.name + ", " +
                this.firstName + ", " +
                this.lastName + ", " +
                this.phones + ", " +
                this.address;
    }
}