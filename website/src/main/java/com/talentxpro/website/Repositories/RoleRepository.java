package com.talentxpro.website.Repositories;


import com.talentxpro.website.models.AppRole;
import com.talentxpro.website.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);

}