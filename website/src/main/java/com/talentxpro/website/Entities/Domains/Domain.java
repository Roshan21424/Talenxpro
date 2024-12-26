package com.talentxpro.website.Entities.Domains;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "domains")
public class Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="domain_id",nullable = false)
    private Long id;

    @Column(name="domain_name",nullable = false)
    private String name;

    @Column(name="domain_description",nullable = false)
    private String description;

    @Column(name = "domain_is_active", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubDomain> subdomains = new ArrayList<>();

    public Domain(Long id, String name, String description, Boolean isActive, List<SubDomain> subdomains) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.subdomains = subdomains;
    }

    public Domain() {
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public List<SubDomain> getSubdomains() {
        return subdomains;
    }

    public void setSubdomains(List<SubDomain> subdomains) {
        this.subdomains = subdomains;
    }

    @Override
    public String toString() {
        return "Domain{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                ", subdomains=" + subdomains +
                '}';
    }
}
