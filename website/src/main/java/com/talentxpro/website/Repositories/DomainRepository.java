package com.talentxpro.website.Repositories;

import com.talentxpro.website.Entities.Domains.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface DomainRepository extends JpaRepository<Domain,Long> {

    @Query(value = "SELECT * FROM domains WHERE domain_name = :name", nativeQuery = true)
    Optional<Domain> findByName(@Param("name") String name);
;


}
