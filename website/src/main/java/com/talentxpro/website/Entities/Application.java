package com.talentxpro.website.Entities;

import com.talentxpro.website.Entities.Domains.SubDomain;
import jakarta.persistence.*;

@Entity
@Table(name="application")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long application_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user_id;

    @OneToOne
    @JoinColumn(name = "subdomain_id")
    private SubDomain subdomain_id;


    public Application(Long application_id, User user_id, SubDomain subdomain_id) {
        this.application_id = application_id;
        this.user_id = user_id;
        this.subdomain_id = subdomain_id;
    }

    public Application() {
    }

    @Override
    public String toString() {
        return "Application{" +
                "application_id=" + application_id +
                ", user_id=" + user_id +
                ", subdomain_id=" + subdomain_id +
                '}';
    }

    public Long getApplication_id() {
        return application_id;
    }

    public void setApplication_id(Long application_id) {
        this.application_id = application_id;
    }

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    public SubDomain getSubdomain_id() {
        return subdomain_id;
    }

    public void setSubdomain_id(SubDomain subdomain_id) {
        this.subdomain_id = subdomain_id;
    }
}
