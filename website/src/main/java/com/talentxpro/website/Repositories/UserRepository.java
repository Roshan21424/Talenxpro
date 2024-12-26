package com.talentxpro.website.Repositories;

import com.talentxpro.website.Entities.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);

    // Use Optional to avoid null returns, making the code more robust
    Optional<User> findByEmail(String email);

    // Change this to a more conventional name
    Optional<User> findByUserName(String userName);
}

