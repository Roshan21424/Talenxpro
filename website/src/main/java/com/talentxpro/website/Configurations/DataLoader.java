package com.talentxpro.website.Configurations;

import com.talentxpro.website.Entities.Domains.Domain;
import com.talentxpro.website.Entities.Domains.SubDomain;
import com.talentxpro.website.Repositories.DomainRepository;
import com.talentxpro.website.Repositories.SubDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class DataLoader implements CommandLineRunner {

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private SubDomainRepository subDomainRepository;

    @Override
    public void run(String... args) throws Exception {
        List<DomainData> domainsData = new ArrayList<>();

        // Adding domain and subdomain data
        domainsData.add(
                new DomainData("Artificial Intelligence", "A field of computer science that aims to create machines that can perform tasks that typically require human intelligence.",
                new ArrayList<>(
                        Arrays.asList(
                           new SubDomainData("Machine Learning", "A subset of AI focused on algorithms and statistical models to perform tasks without explicit programming."),
                           new SubDomainData("Deep Learning", "A subset of machine learning, using neural networks to solve complex problems.")
                        )
                       )
                )
        );

        domainsData.add(new DomainData("Full Stack Development", "Building websites and web applications.",
            new ArrayList<>(Arrays.asList(
                new SubDomainData("MERN Stack", "Learn building web applications using MongoDB, ExpressJs, ReactJs, NodeJs."),
                new SubDomainData("Spring Stack", "Learn building web applications using Spring and SpringBoot."),
                new SubDomainData("Django Stack", "Learn building web applications using Django.")
            ))));

        domainsData.add(new DomainData("Cloud ", "Build Technology using cloud.",
                new ArrayList<>(Arrays.asList(
                        new SubDomainData("Aws", "Leverage AWS to deploy scalable applications with cloud-based resources."),
                        new SubDomainData("Azure", "Utilize Azure to build, deploy, and manage applications in the cloud."),
                        new SubDomainData("Google CLoud Platform", "Use Google Cloud to develop and scale applications with powerful cloud services.")
                ))));

        // Iterate over the list and insert or update domains and subdomains
        for (DomainData domainData : domainsData) {
            // Check if the domain already exists
            Optional<Domain> existingDomain = domainRepository.findByName(domainData.getDomainName());
            Domain domain;
            //if the Domain exits then update the description else create a new Domain
            if (existingDomain.isPresent()) {
                // Update the existing domain
                domain = existingDomain.get();
                domain.setDescription(domainData.getDomainDescription());
            } else {
                // Create a new domain
                domain = new Domain();
                domain.setName(domainData.getDomainName());
                domain.setDescription(domainData.getDomainDescription());
                domain.setActive(true);
            }

            // Save or update the domain
            domainRepository.save(domain);

            // Iterate over subdomains for each domain
            for (SubDomainData subDomainData : domainData.getSubDomains()) {
                // Check if the subdomain already exists
                Optional<SubDomain> existingSubDomain = subDomainRepository.findByNameAndDomain(subDomainData.getSubDomainName(), domain);
                SubDomain subDomain;
                if (existingSubDomain.isPresent()) {
                    // Update the existing subdomain
                    subDomain = existingSubDomain.get();
                    subDomain.setDescription(subDomainData.getSubDomainDescription());
                } else {
                    // Create a new subdomain
                    subDomain = new SubDomain();
                    subDomain.setName(subDomainData.getSubDomainName());
                    subDomain.setDescription(subDomainData.getSubDomainDescription());
                    subDomain.setIsActive(true);
                    subDomain.setDomain(domain);  // Associate the subdomain with the domain
                }

                // Save or update the subdomain
                subDomainRepository.save(subDomain);
            }
        }

        // Log the data insertion/updating
        System.out.println("Domains and Subdomains have been successfully inserted/updated!");
    }

    // Inner classes for Domain data representation
    static class DomainData {
        private String domainName;
        private String domainDescription;
        private List<SubDomainData> subDomains;

        public DomainData(String domainName, String domainDescription, List<SubDomainData> subDomains) {
            this.domainName = domainName;
            this.domainDescription = domainDescription;
            this.subDomains = subDomains;
        }

        public String getDomainName() {
            return domainName;
        }

        public String getDomainDescription() {
            return domainDescription;
        }

        public List<SubDomainData> getSubDomains() {
            return subDomains;
        }
    }

    // Inner classes for SubDomain data representation
    static class SubDomainData {
        private String subDomainName;
        private String subDomainDescription;

        public SubDomainData(String subDomainName, String subDomainDescription) {
            this.subDomainName = subDomainName;
            this.subDomainDescription = subDomainDescription;
        }

        public String getSubDomainName() {
            return subDomainName;
        }

        public String getSubDomainDescription() {
            return subDomainDescription;
        }
    }
}
