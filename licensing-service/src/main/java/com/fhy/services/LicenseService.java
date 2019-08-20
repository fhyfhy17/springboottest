package com.fhy.services;

import com.fhy.clients.OrganizationDiscoveryClient;
import com.fhy.clients.OrganizationFeignClient;
import com.fhy.clients.RestTemplateClient;
import com.fhy.controller.config.ServiceConfig;
import com.fhy.model.License;
import com.fhy.model.Organization;
import com.fhy.repository.LicenseRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    
    
    @HystrixCommand
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
                organization = restTemplateClient.getOrganization(organizationId);
        }
        
        return organization;
    }
    
    @HystrixCommand(
            fallbackMethod="buildFallbackLicenseList",
            commandProperties={
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="7000"),
                    @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="12"), //在十秒内连续调用的次数要达到12次
                    @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="75"),//到达12次，统计失败率，如果超过75%
                    @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="7000"),//如果超过75%，休息7秒，7秒钟内的请求都返回失败，7秒钟后尝试可否恢复
                    @HystrixProperty(name="metrics.rollingStats.timeInMilliseconds", value="15000"), //尝试的 窗口时间，必须是metrics.rollingStats.numBuckets的整数倍
                    @HystrixProperty(name="metrics.rollingStats.numBuckets", value="5") //尝试的 统计桶个数
    },
    threadPoolKey="licenseByOrgThreadPool",
            threadPoolProperties={
                    @HystrixProperty(name="coreSize",value="30"),
                    @HystrixProperty(name="maxQueueSize",value="10")
                    
            }
    )
    public List<License> getLicensesByOrg(String organizationId){
        randomOvertime();
        return licenseRepository.findByOrganizationId(organizationId);
    }
    
    private void randomOvertime(){
        int i=RandomUtils.nextInt(2);
        if(i==0){
            try
            {
                Thread.sleep(8000);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public List<License> buildFallbackLicenseList(String organizationId){
        List<License> fallbackList = new ArrayList<>();
        License license = new License()
                .withId("0000000-00-00000")
                .withOrganizationId( organizationId )
                .withProductName("Sorry no licensing information currently available");
        
        fallbackList.add(license);
        return fallbackList;
    }
    
    public License getLicense(String organizationId, String licenseId) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        license.setComment(config.getExampleProperty());

        return license;
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