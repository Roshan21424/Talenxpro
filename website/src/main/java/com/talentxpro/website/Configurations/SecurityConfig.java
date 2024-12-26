package com.talentxpro.website.Configurations;

import com.talentxpro.website.Configurations.SecurityConfigurations.OAuth2LoginSuccessHandler;
import com.talentxpro.website.JwtUtils.AuthTokenFilter;
import com.talentxpro.website.Repositories.RoleRepository;
import com.talentxpro.website.Repositories.UserRepository;
import com.talentxpro.website.models.AppRole;
import com.talentxpro.website.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;


@Configuration
public class SecurityConfig {

    @Autowired
    @Lazy
    OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http.csrf(csrf ->
                    csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                            .ignoringRequestMatchers("/api/auth/public/**")
            );
            // Allow unauthenticated access to the login page and GitHub OAuth2 login page
            http.authorizeHttpRequests(requests -> requests
                    .requestMatchers("/login.html").permitAll() // Custom login page
                    .requestMatchers("/login/oauth2").permitAll() // Custom OAuth2 login page
                    .requestMatchers("/api/csrf-token").permitAll()
                    .requestMatchers("/oauth2/authorization/github").permitAll() // GitHub OAuth2 login
                    .anyRequest().authenticated() // All other requests should be authenticated
            );

            // OAuth2 login configuration
            http.oauth2Login(oauth2 -> oauth2
                    .loginPage("/login/authenticate") // Custom OAuth2 login page
                    .successHandler(oAuth2LoginSuccessHandler) // Custom success handler after OAuth2 login
            );

            // Form login configuration for custom login page
            http.formLogin(formLogin -> formLogin
                    .loginPage("/login/authenticate") // Custom login page URL
                    .loginProcessingUrl("/login") // The URL to handle the login form submission
                    .defaultSuccessUrl("/homepage.html", true) // Redirect to homepage after successful login
                    .permitAll() // Allow all users to access login page
            );

            // Disable CSRF protection for OAuth2 paths if needed, and add custom JWT filter
            http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }
    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository,
                                      UserRepository userRepository) {
        return args -> {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));


        };


    }
    }






