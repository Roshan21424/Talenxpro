package com.talentxpro.website.Entities;

import com.talentxpro.website.Entities.Domains.SubDomain;
import com.talentxpro.website.Entities.Users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "application")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
