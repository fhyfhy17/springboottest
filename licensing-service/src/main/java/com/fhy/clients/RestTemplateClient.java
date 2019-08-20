package com.fhy.clients;

import com.fhy.model.Organization;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/***
 * 负载均衡查找服务
 */
@Component
public class RestTemplateClient
{
	@Autowired
	private RestTemplate restTemplate;
	
	@HystrixCommand(commandProperties={
			@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="7000")
	})
	public Organization getOrganization(String organizationId){

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

		ResponseEntity<Organization> exchange=restTemplate.exchange("http://organizationservice/v1/organizations/{organizationId}"
				,HttpMethod.GET
				,null
				,Organization.class
				,organizationId);
		return exchange.getBody();
	}
}
