package com.fidelity.weather.restcontroller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerErrorException;

import com.fidelity.weather.models.LatLong;
import com.fidelity.weather.models.WeatherForecast;
import com.fidelity.weather.services.WeatherDatabaseException;
import com.fidelity.weather.services.WeatherService;
import com.fidelity.weather.services.WeatherServiceImpl;

class WeatherControllerPojoUnitTest {

	@InjectMocks
	private WeatherController controller;
	@Mock
	private Logger mockLogger;
	@Mock
	private RestTemplate mockRestTemplate;
	@Mock
	private WeatherServiceImpl mockService;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void testGetForecast_CheckSuccessCodeValue() {
		String city = "Honolulu";
		String state = "HI";
		String country="US";
		String lat = "21.0";
		String lon = "-158.0";

		when(mockService.getLatitudeAndLongitude(city, state,country))
			.thenReturn(new LatLong(lat, lon));

		WeatherForecast forecast = new WeatherForecast();
		forecast.setDetailedForecast("Sunny, high 85");
		when(mockRestTemplate.getForEntity(WeatherController.FORECAST_URL_BASE + lat + "," + lon, WeatherForecast.class))
			.thenReturn(ResponseEntity.ok(forecast));

		ResponseEntity<WeatherForecast> response = controller.getForecast(city, state,country);
		
		assertEquals(200, response.getStatusCodeValue());
	}
	
	@Test
	public void testGetForecast_JsonValueReturnedIsCorect() {
		String city = "Honolulu";
		String state = "HI";
		String country="US";
		String lat = "21.0";
		String lon = "-158.0";

		when(mockService.getLatitudeAndLongitude(city, state,country))
			.thenReturn(new LatLong(lat, lon));

		WeatherForecast forecast = new WeatherForecast();
		forecast.setDetailedForecast("Sunny, high 85");
		when(mockRestTemplate.getForEntity(WeatherController.FORECAST_URL_BASE + lat + "," + lon, WeatherForecast.class))
			.thenReturn(ResponseEntity.ok(forecast));

		ResponseEntity<WeatherForecast> response = controller.getForecast(city, state,country);
		
		
		assertEquals("Sunny, high 85", response.getBody().getDetailedForecast());
	}

	@Test
	public void testGetForecast_CityNotFound() {
		String city = "Loose Gravel";
		String state = "NY";
		String country="US";

		when(mockService.getLatitudeAndLongitude(city, state,country))
			.thenReturn(null);
		
		ResponseEntity<WeatherForecast> response = controller.getForecast(city, state,country);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	public void testGetForecast_StateNotFound() {
		String city = "Loose Gravel";
		String state = "N";
		String country="US";

		when(mockService.getLatitudeAndLongitude(city, state,country))
			.thenReturn(null);
		
		ResponseEntity<WeatherForecast> response = controller.getForecast(city, state,country);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	public void testGetForecast_CountryNotFound() {
		String city = "Loose Gravel";
		String state = "NY";
		String country="U";

		when(mockService.getLatitudeAndLongitude(city, state,country))
			.thenReturn(null);
		
		ResponseEntity<WeatherForecast> response = controller.getForecast(city, state,country);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	public void testGetForecast_CityIsNull() {
		String city = null;
		String state = "NY";
		String country="US";

		when(mockService.getLatitudeAndLongitude(city, state,country))
			.thenReturn(null);
		
		ResponseEntity<WeatherForecast> response = controller.getForecast(city, state,country);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void testGetForecast_StateIsNull() {
		String city = "Loose Gravel";
		String state = null;
		String country="US";

		when(mockService.getLatitudeAndLongitude(city, state,country))
			.thenReturn(null);
		
		ResponseEntity<WeatherForecast> response = controller.getForecast(city, state,country);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void testGetForecast_CountryIsNull() {
		String city = "Loose Gravel";
		String state = "HE";
		String country=null;

		when(mockService.getLatitudeAndLongitude(city, state,country))
			.thenReturn(null);
		
		ResponseEntity<WeatherForecast> response = controller.getForecast(city, state,country);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	

	
}
