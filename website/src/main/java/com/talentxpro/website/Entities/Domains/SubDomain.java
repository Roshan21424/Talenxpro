package com.talentxpro.website.Entities.Domains;


import com.talentxpro.website.Entities.Application;
import jakarta.persistence.*;

//A SubDomain is the child class for Domain
//It is used to indicate subdomains
//example: Domain(Full Stack)->SubDomain(Django)
@Entity
@Table(name="subdomains")
public class SubDomain{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="subdomain_id",nullable = false)
    private Long id;

    @Column(name="subdomain_name",nullable = false)
    private String name;

    @Column(name="subdomain_description",nullable = false)
    private String description;

    @Column(name = "subdomain_is_active", nullable = false)
    private Boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "domain_id", nullable = false)
    private Domain domain;

    @OneToOne(mappedBy = "subdomain_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Application application;

    public SubDomain(Long id, String name, String description, Boolean isActive, Domain domain) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.domain = domain;
    }

    public SubDomain() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        return "SubDomain{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                ", domain=" + domain +
                '}';
    }
}
