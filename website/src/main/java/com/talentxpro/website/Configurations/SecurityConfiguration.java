package com.talentxpro.website.Configurations;

import com.talentxpro.website.Configurations.JwtUtilities.AuthEntryPoint;
import com.talentxpro.website.Configurations.JwtUtilities.AuthTokenFilter;
import com.talentxpro.website.Configurations.SuccessHandlers.OAuth2LoginSuccessHandler;
import com.talentxpro.website.Repositories.RoleRepository;
import com.talentxpro.website.Repositories.UserRepository;
import com.talentxpro.website.models.RoleDTO.AppRole;
import com.talentxpro.website.models.RoleDTO.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfiguration {

    @Autowired
    private AuthEntryPoint unauthorizedHandler;
    @Autowired
    @Lazy
    OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter()    {
        return new AuthTokenFilter();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http.cors(Customizer.withDefaults()).csrf(csrf ->
                    csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                            .ignoringRequestMatchers("/api/auth/public/**")
            );
            // Allow unauthenticated access to the login page and GitHub OAuth2 login page
            http.authorizeHttpRequests(requests -> requests
                    .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                    .requestMatchers("/login/oauth2").permitAll() // Custom OAuth2 login page
                    .requestMatchers("/api/csrf-token").permitAll()
                    .requestMatchers("/api/auth/public/**").permitAll()
                    .requestMatchers("/oauth2/authorization/github").permitAll() // GitHub OAuth2 login
                    .anyRequest().authenticated() // All other requests should be authenticated
            );

            // OAuth2 login configuration
            http.oauth2Login(oauth2 -> oauth2
                    .loginPage("/login/authenticate") // Custom OAuth2 login page
                    .successHandler(oAuth2LoginSuccessHandler) // Custom success handler after OAuth2 login
            );
            http.exceptionHandling(exception
                    -> exception.authenticationEntryPoint(unauthorizedHandler));
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






