package com.talentxpro.website.Services;




import com.talentxpro.website.Entities.User;
import com.talentxpro.website.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByEmail(String email){
        return userRepository.findUserFromEmailId(email);
    }

    public Optional<User> findByUsername(String name){
        return userRepository.findByUserName(name);
    }

    public User registerUser(User user){
        if (user.getUserPassword() != null)
            user.setUserPassword(user.getUserPassword());
        return userRepository.save(user);
    }

}
