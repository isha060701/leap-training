package com.fidelity.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

// IMPORTANT: don't add any annotations to this class
@SpringBootApplication
public class WeatherServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(WeatherServiceApplication.class, args);
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

