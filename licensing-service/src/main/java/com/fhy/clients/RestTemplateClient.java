package com.fhy.clients;

import com.fhy.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateClient
{
	@Autowired
	private RestTemplate restTemplate;
	
	public Organization getOrganization(String organizationId){
		ResponseEntity<Organization> exchange=restTemplate.exchange("http://organizationservice/v1/organizations/{organizationId}"
				,HttpMethod.GET
				,null
				,Organization.class
				,organizationId);
		return exchange.getBody();
	}
}
