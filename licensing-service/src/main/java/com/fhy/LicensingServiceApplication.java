package com.fhy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class LicensingServiceApplication
{
	
	@RequestMapping("/")
	String home(String name)
	{
		return "Hello World!";
	}
	public static void main(String[] args)
	{
		SpringApplication.run(LicensingServiceApplication.class,args);
	}

}
