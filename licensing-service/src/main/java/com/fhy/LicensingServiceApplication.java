package com.fhy;

import com.fhy.events.model.OrganizationChangeModel;
import com.fhy.utils.UserContextInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@Slf4j
@SpringBootApplication
@EnableEurekaClient    //服务发现
@EnableDiscoveryClient //只有普通手动服务调用才需要
@EnableFeignClients    //只有Feign服务调用才需要
@EnableCircuitBreaker  //断路器
@EnableBinding(Sink.class)
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
	
	@StreamListener(Sink.INPUT)
	public void loggerSink(OrganizationChangeModel orgChange) {
		log.info("收到一个事件 for organization id = {} action = {} 关联ID = {}", orgChange.getOrganizationId(),orgChange.getAction(),orgChange.getCorrelationId());
	}
}
