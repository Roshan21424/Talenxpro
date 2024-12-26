package com.talentxpro.website.Services;

import com.talentxpro.website.Entities.Users.User;
import com.talentxpro.website.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User findByUsername(String name){
        return userRepository.findByUserName(name).get();
    }

    public User registerUser(User user){
        if (user.getPassword() != null)
            user.setPassword(user.getPassword());
        return userRepository.save(user);
    }

}
