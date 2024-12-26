package com.talentxpro.website.Controllers;

import com.talentxpro.website.Entities.Application;
import com.talentxpro.website.Entities.Domains.Domain;
import com.talentxpro.website.Entities.Domains.SubDomain;
import com.talentxpro.website.Entities.User;
import com.talentxpro.website.Repositories.ApplicationRepository;
import com.talentxpro.website.Repositories.DomainRepository;
import com.talentxpro.website.Repositories.SubDomainRepository;
import com.talentxpro.website.Repositories.UserRepository;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


@Controller
public class MainController{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubDomainRepository subDomainRepository;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private ApplicationRepository applicationRepository;
    @GetMapping("/homepage")
    public void function1(HttpServletResponse response) throws Exception{
        response.sendRedirect("/homepage.html");
    }


        @GetMapping("/login/authenticate")
        public String customOauth2LoginPage(Model model) {
            // Add the necessary data if required, e.g., OAuth2 client details
            return "login"; // This is your custom OAuth2 login page template
        }


    @PostMapping("/app/apply")
    public String applyForInternship(@RequestBody Map<String, String> applicationData) {
        OAuth2AuthenticationToken authenticationToken =
                (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

// Get the principal (OAuth2User)
        OAuth2User principal = authenticationToken.getPrincipal();

// Retrieve the real name
        String name = (String) principal.getAttributes().get("name");

        String domain = applicationData.get("domain");
        String subdomain = applicationData.get("subdomain");

        System.out.println(name);
        System.out.println(domain);
        System.out.println(subdomain);

        User user = userRepository.findByUserName(name).get();
        Domain Domain = domainRepository.findByName(domain).get();
        SubDomain subDomain = subDomainRepository.findByNameAndDomain(subdomain,Domain).get();

        Application application = new Application();
        application.setUser_id(user);
        application.setSubdomain_id(subDomain);
        applicationRepository.save(application);
        // Process the application
        return "ram";
    }

}
