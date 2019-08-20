package com.fhy;

import com.fhy.utils.UserContextInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableEurekaClient    //服务发现
@EnableDiscoveryClient //只有普通手动服务调用才需要
@EnableFeignClients    //只有Feign服务调用才需要
@EnableCircuitBreaker  //断路器
public class LicensingServiceApplication
{
	
	public static void main(String[] args)
	{
		SpringApplication.run(LicensingServiceApplication.class,args);
	}
	
	@LoadBalanced
	@Bean
	public RestTemplate getRestTemplate()
	{
		RestTemplate template=new RestTemplate();
		template.getInterceptors().add(new UserContextInterceptor());
		return new RestTemplate();
	}
}
