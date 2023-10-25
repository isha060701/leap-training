package com.fidelity.india.secondary.assessment;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
//tell Spring Boot where to scan for annotated components
@ComponentScan(basePackages={"com.fidelity.india.secondary.integration", "com.fidelity.india.secondary.restcontroller", "com.fidelity.india.secondary.services"})
//tell MyBatis where to scan for mapping interface files
@MapperScan(basePackages="com.fidelity.india.secondary.integration.mapper")
public class SecondaryAssessmentStarterApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecondaryAssessmentStarterApplication.class, args);
	}

	/**
	 * This method creates a Logger that can be autowired in other classes:{@code
	 *    @Autowired 
	 *    private Logger logger;
	 }*/
	@Bean
	@Scope("prototype")
	Logger createLogger(InjectionPoint ip) {
	    Class<?> classThatWantsALogger = ip.getField().getDeclaringClass();
	    return LoggerFactory.getLogger(classThatWantsALogger);
	}
	
	/**
	 * This method creates a RestTemplate that can be autowired in other classes:{@code
	 *    @Autowired 
	 *    private RestTemplate restTemplate;
	 }*/
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
