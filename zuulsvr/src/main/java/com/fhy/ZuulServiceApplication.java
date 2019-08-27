package com.fhy;

import brave.sampler.DeclarativeSampler;
import brave.sampler.Sampler;
import com.fhy.utils.UserContextInterceptor;
import com.google.common.collect.Collections2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class ZuulServiceApplication
{
	
	public static void main(String[] args)
	{
		SpringApplication.run(ZuulServiceApplication.class,args);
	}
	
	@LoadBalanced
	@Bean
	public RestTemplate getRestTemplate(){
		RestTemplate template = new RestTemplate();
		template.getInterceptors().add(new UserContextInterceptor());
		return template;
	}
	
	@Bean
	public Sampler defaultSampler(){
		return Sampler.ALWAYS_SAMPLE;
	}
}
