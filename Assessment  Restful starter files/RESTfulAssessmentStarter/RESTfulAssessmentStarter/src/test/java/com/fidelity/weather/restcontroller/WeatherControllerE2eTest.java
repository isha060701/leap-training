package com.fidelity.weather.restcontroller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.fidelity.weather.models.WeatherForecast;

/**
 * IMPORTANT: be sure to run the database initialization scripts in SQL Developer
 * before running these test cases:
 *	 src/main/resources/schema.sql
 *	 src/main/resources/data.sql
 */

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class WeatherControllerE2eTest {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	public void testGetForecast_CheckSuccessStatusCode() {
		String request = "/weather/forecast/US/HI/Honolulu";
		WeatherForecast expectedForecast = 
			new WeatherForecast(21.3099, -157.8581, "F", 83, 71, "Sunny",
				"Sunny, with a high near 83. East wind around 12 mph.");

		ResponseEntity<WeatherForecast> response = 
				restTemplate.getForEntity(request, WeatherForecast.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		
	}
	
	@Test
	public void testGetForecast_ReceivedCorrectJsonObject() {
		String request = "/weather/forecast/US/HI/Honolulu";
		WeatherForecast expectedForecast = 
			new WeatherForecast(21.3099, -157.8581, "F", 83, 71, "Sunny",
				"Sunny, with a high near 83. East wind around 12 mph.");

		ResponseEntity<WeatherForecast> response = 
				restTemplate.getForEntity(request, WeatherForecast.class);
		
		
		WeatherForecast forecast = response.getBody();
		assertEquals(expectedForecast, forecast);
	}
	
	@Test
	public void testGetForecast_UnknownCityNotFound () {
		String request = "/weather/forecast/US/HI/Funkytown";

		ResponseEntity<WeatherForecast> response = 
				restTemplate.getForEntity(request, WeatherForecast.class);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	

	

}
