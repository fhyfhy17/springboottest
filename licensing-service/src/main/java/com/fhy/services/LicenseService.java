package com.fhy.services;

import com.fhy.clients.OrganizationDiscoveryClient;
import com.fhy.clients.OrganizationFeignClient;
import com.fhy.clients.RestTemplateClient;
import com.fhy.controller.config.ServiceConfig;
import com.fhy.model.License;
import com.fhy.model.Organization;
import com.fhy.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


import java.util.List;
import java.util.UUID;

@Service
public class LicenseService {

    @Autowired
    ServiceConfig config;
    @Autowired
    private LicenseRepository licenseRepository;
    @Autowired
    private OrganizationDiscoveryClient organizationDiscoveryClient;
    @Autowired
    private RestTemplateClient restTemplateClient;
    @Autowired
	private OrganizationFeignClient organizationFeignClient;
    
    
    public License getLicense(String organizationId, String licenseId,String clientType) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        Organization organization=retrieveOrgInfo(organizationId,clientType);
      
        return license.withOrganizationName(organization.getName())
                .withContactName(organization.getContactName())
                .withContactEmail(organization.getContactEmail())
                .withContactPhone(organization.getContactPhone())
                .withComment(config.getExampleProperty());
    }
    
    
    
    private Organization retrieveOrgInfo(String organizationId,String clientType){
        Organization organization = null;
        
        switch (clientType) {
            case "feign":
                System.out.println("I am using the feign client");
                organization = organizationFeignClient.getOrganization(organizationId);
                break;
            case "rest":
                System.out.println("I am using the rest client");
                organization = restTemplateClient.getOrganization(organizationId);
                break;
            case "discovery":
                System.out.println("I am using the discovery client");
                organization = organizationDiscoveryClient.getOrganization(organizationId);
                break;
            default:
                //organization = organizationRestClient.getOrganization(organizationId);
        }
        
        return organization;
    }
    
    public License getLicense(String organizationId, String licenseId) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        license.setComment(config.getExampleProperty());

        return license;
    }

    public List<License> getLicensesByOrg(String organizationId) {
        return licenseRepository.findByOrganizationId(organizationId);
    }

    public void saveLicense(License license) {
        license.setLicenseId(UUID.randomUUID().toString());
        licenseRepository.save(license);

    }

    public void updateLicense(License license) {
        licenseRepository.save(license);
    }

    public void deleteLicense(License license) {
        licenseRepository.delete(license);
    }
}