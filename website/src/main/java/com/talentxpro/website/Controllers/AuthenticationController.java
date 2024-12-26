package com.talentxpro.website.Controllers;

import com.talentxpro.website.Configurations.JwtUtilities.JwtUtils;
import com.talentxpro.website.Entities.Users.User;
import com.talentxpro.website.Repositories.RoleRepository;
import com.talentxpro.website.Repositories.UserRepository;
import com.talentxpro.website.Services.UserService;
import com.talentxpro.website.models.RoleDTO.AppRole;
import com.talentxpro.website.models.RoleDTO.Role;
import com.talentxpro.website.models.Security.UserDetailsImplementation;
import com.talentxpro.website.models.SigningDTO.Request.LoginRequest;
import com.talentxpro.website.models.SigningDTO.Request.SignupRequest;
import com.talentxpro.website.models.SigningDTO.Response.LoginResponse;
import com.talentxpro.website.models.SigningDTO.Response.MessageResponse;
import com.talentxpro.website.models.SigningDTO.Response.UserInfoResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserService userService;

    //signup endpoint
    @PostMapping("/public/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        //checking if the user is already present or no
        //if the user already exist with the name else send the "Error:Username is already taken!" message
        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        //if the user already exist with the email else send the "Error:Email is already in use!" message
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        //after checking that the user is new
        //Create new user's account
        //setting the name,email,password
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),encoder.encode(signUpRequest.getPassword()));

        //get role
        Set<String> strRoles = signUpRequest.getRole();
        Role role;

        //if role does not exist or empty
        //then it is a normal user ->
        //  set role as ROLE_USER
        //else it may be user or admin->
        //  check if user and set role = ROLE_USER
        //  check if admin and set role = ROLE_ADMIN
        if (strRoles == null || strRoles.isEmpty()) {
            role = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        } else {
            String roleStr = strRoles.iterator().next();
            if (roleStr.equals("admin")) {
                role = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            } else {
                role = roleRepository.findByRoleName(AppRole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            }

            //setting other properties
            user.setAccountNonLocked(true);
            user.setAccountNonExpired(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);
            user.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
            user.setAccountExpiryDate(LocalDate.now().plusYears(1));
            user.setTwoFactorEnabled(false);
            user.setSignUpMethod("email");
        }
        //set the user role
        user.setRole(role);
        //save the user
        userRepository.save(user);
        //return with the ok status code
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }



    @PostMapping("/public/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication;
        //try to load the user from with the username and password else return the NOT_FOUND status code
        try {
            System.out.println(loginRequest.getPassword());
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        //Set the user in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //get the UserDetails object from the authentication object
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        //generate JWT token
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
        // Collect roles from the UserDetails object
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        //Prepare the response body, now including the JWT token directly in the body
        LoginResponse response = new LoginResponse(userDetails.getUsername(),roles,jwtToken);
        // Return the response entity with the JWT token included in the response body
        return ResponseEntity.ok(response);
    }


    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

               UserInfoResponse response = new UserInfoResponse(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.isAccountNonLocked(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                user.getCredentialsExpiryDate(),
                user.getAccountExpiryDate(),
                user.isTwoFactorEnabled(),
                roles
               );

        return ResponseEntity.ok().body(response);
    }
}
