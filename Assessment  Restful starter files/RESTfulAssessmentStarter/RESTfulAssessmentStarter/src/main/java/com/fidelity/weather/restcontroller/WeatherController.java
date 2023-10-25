package com.fidelity.weather.restcontroller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fidelity.weather.models.LatLong;
import com.fidelity.weather.models.WeatherForecast;
import com.fidelity.weather.services.WeatherDatabaseException;
import com.fidelity.weather.services.WeatherServiceImpl;

@RestController 
@RequestMapping("/weather")
public class WeatherController {

	public static final String FORECAST_URL_BASE = "http://localhost:8088/forecast/";

	@Autowired
	private Logger logger;  
	
	@Autowired
	private WeatherServiceImpl dao;  
	
	@Autowired
	private RestTemplate restTemplate;  
	
	@GetMapping("/forecast/{country}/{state}/{city}")  
	public ResponseEntity<WeatherForecast> getForecast(@PathVariable String city,   
											           @PathVariable String state,
											           @PathVariable String country) {
		
		if (city==null|| state==null || country==null) {
			logger.error("state or city or country cant be null");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		String errorReason = "problem getting forecast for " + city + ", " + state;

		try {  
			ResponseEntity<WeatherForecast> forecastResponse;	

			LatLong latLong = dao.getLatitudeAndLongitude(city, state,country);

			if (latLong != null) {  
				WeatherForecast detailedForecast = 
						getForecastFromBackend(latLong.getLatitude(), latLong.getLongitude());

				forecastResponse = ResponseEntity.ok(detailedForecast);
			}
			else {
				logger.warn("getForecast: " + city + ", " + state + " not found");
				forecastResponse = ResponseEntity.notFound().build();
			}
			return forecastResponse;
		}
		catch (WeatherDatabaseException e) {
			logger.error("cannot getForecast({}, {}): {}", city, state, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private WeatherForecast getForecastFromBackend(String latitude, String longitude) {
		logger.debug("getForecast({},{}): enter", latitude, longitude);

		String forecastUrl = FORECAST_URL_BASE + latitude + "," + longitude;

		
		ResponseEntity<WeatherForecast> forecastResponse = 
				restTemplate.getForEntity(forecastUrl, WeatherForecast.class);

		if (forecastResponse.getStatusCode() != HttpStatus.OK) {
			String message = "Error getting forecast for " + latitude + "," + longitude;
			throw new WeatherDatabaseException(message);
		}

		WeatherForecast forecast = forecastResponse.getBody();

		return forecast;
	}
}
