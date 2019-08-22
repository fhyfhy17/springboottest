package com.fhy;

import com.fhy.utils.UserContextInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


//import org.springframework.cloud.netflix.feign.EnableFeignClients;


@SpringBootApplication
@EnableEurekaClient
//@EnableFeignClients
@EnableBinding(Source.class)
public class OrganizationApplication
{
    public static void main(String[] args) {
        SpringApplication.run(OrganizationApplication.class, args);
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
