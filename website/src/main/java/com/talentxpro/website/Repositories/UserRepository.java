package com.talentxpro.website.Repositories;

import com.talentxpro.website.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query(value = "SELECT * FROM user WHERE user_email_id = :emailid", nativeQuery = true)
    public Optional<User> findUserFromEmailId(@Param("emailid") String emailid);


    @Query(value = "SELECT * FROM user WHERE user_name = :name", nativeQuery = true)
    public Optional<User> findByUserName(@Param("name") String name);
}
