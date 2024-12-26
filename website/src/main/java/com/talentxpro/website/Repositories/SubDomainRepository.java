package com.talentxpro.website.Repositories;

import com.talentxpro.website.Entities.Domains.Domain;
import com.talentxpro.website.Entities.Domains.SubDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubDomainRepository extends JpaRepository<SubDomain,Integer> {


    Optional<SubDomain> findByNameAndDomain(String name, Domain domain);
}
