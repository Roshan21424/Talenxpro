package com.talentxpro.website.Configurations.SecurityConfigurations;


import com.talentxpro.website.Entities.User;
import com.talentxpro.website.JwtUtils.JwtUtils;
import com.talentxpro.website.Repositories.RoleRepository;
import com.talentxpro.website.Services.SecurityServices.UserDetailsImpl;
import com.talentxpro.website.Services.UserService;
import com.talentxpro.website.models.AppRole;
import com.talentxpro.website.models.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private final UserService userService;

    @Autowired
    private final JwtUtils jwtUtils;

    @Autowired
    RoleRepository roleRepository;


    String username;
    String idAttributeKey;

    public OAuth2LoginSuccessHandler(UserService userService) {
        this.userService = userService;
        jwtUtils = null;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {


        //creating in an Oauth2 authentication token(using the authentication object)
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        System.out.println(oAuth2AuthenticationToken);

        //checking it the token is of the github or google
        if ("github".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()) || "google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {

            //getting the principal
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            System.out.println("The principle is : "+principal);

            //fetching the attributes from the principle
            Map<String, Object> attributes = principal.getAttributes();
            System.out.println("The attributes are : "+attributes);

            String email = attributes.getOrDefault("email", "").toString();
            String name = attributes.getOrDefault("name", "").toString();

            System.out.println("The attributes are a: "+email+ name);

            if ("github".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
                username = attributes.getOrDefault("login", "").toString();
                idAttributeKey = "id";
            } else if ("google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
                username = email.split("@")[0];
                idAttributeKey = "sub";
            } else {
                username = "";
                idAttributeKey = "id";
            }




            System.out.println("HELLO OAUTH: " + email + " : " + name + " : " + username);

            //fetch the email from the database of application
            userService.findByEmail(email)
                    .ifPresentOrElse(

                            //if the user is present in the database
                            user -> {

                                //create an oauth user
                        DefaultOAuth2User oauthUser = new DefaultOAuth2User(

                                //specify roles,attributes,idAttributeKey
                                List.of(new SimpleGrantedAuthority(user.getUserRole().getRoleName().name())),
                                attributes,
                                idAttributeKey
                        );

                        //create an authentication object using the OAuth token
                        Authentication securityAuth = new OAuth2AuthenticationToken(
                                oauthUser,
                                List.of(new SimpleGrantedAuthority(user.getUserRole().getRoleName().name())),
                                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()
                        );

                        //setting the Authentication in the security Context
                        SecurityContextHolder.getContext().setAuthentication(securityAuth);

                    },
                            //if the user is not present

                            () -> {
                         //create a user
                        User newUser = new User();

                        // Fetch existing role
                        Optional<Role> userRole = roleRepository.findByRoleName(AppRole.ROLE_USER);

                        // Set existing role
                        if (userRole.isPresent()) {
                            newUser.setUserRole(userRole.get());
                        }
                        else {
                            // Handle the case where the role is not found
                            throw new RuntimeException("Default role not found");
                        }
                        //set the attributes
                        newUser.setUserEmailId(email);
                        newUser.setUserName(name);
                        newUser.setSignUpMethod(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());

                        //save the user in the  database
                        userService.registerUser(newUser);

                        DefaultOAuth2User oauthUser = new DefaultOAuth2User(
                                List.of(new SimpleGrantedAuthority(newUser.getUserRole().getRoleName().name())),
                                attributes,
                                idAttributeKey
                        );
                        Authentication securityAuth = new OAuth2AuthenticationToken(
                                oauthUser,
                                List.of(new SimpleGrantedAuthority(newUser.getUserRole().getRoleName().name())),
                                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()
                        );
                        SecurityContextHolder.getContext().setAuthentication(securityAuth);
                    });
        }
        this.setAlwaysUseDefaultTargetUrl(false);


        // JWT TOKEN LOGIC
        DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oauth2User.getAttributes();

        // Extract necessary attributes
        String email = (String) attributes.get("email");
        System.out.println("OAuth2LoginSuccessHandler: " + username + " : " + email);

        // Create UserDetailsImpl instance
        UserDetailsImpl userDetails = new UserDetailsImpl(
                null,
                username,
                email,
                null,
                false,
                oauth2User.getAuthorities().stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                        .collect(Collectors.toList())
        );

        // Generate JWT token
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
        System.out.println(jwtToken);




        // Create a cookie to store the JWT token
        Cookie jwtCookie = new Cookie("jwtToken", jwtToken);
        jwtCookie.setHttpOnly(true);  // Ensures the cookie is only accessed by the server (prevents XSS)
        jwtCookie.setSecure(true);    // Send only over HTTPS (ensure your app is served over HTTPS)
        jwtCookie.setMaxAge(24 * 60 * 60); // Set cookie expiry (1 day in this case)
        jwtCookie.setPath("/");       // Cookie accessible for the entire application

        // Add the cookie to the response
        response.addCookie(jwtCookie);
        // Optionally set the token in response header
        response.setHeader("Authorization", "Bearer " + jwtToken);
        response.setStatus(HttpServletResponse.SC_OK); // Set status to OK

        response.sendRedirect("/homepage.html");
    }
}
















