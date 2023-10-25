package com.fidelity.weather.restcontroller;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.fidelity.weather.models.LatLong;
import com.fidelity.weather.models.WeatherForecast;
import com.fidelity.weather.services.WeatherDatabaseException;
import com.fidelity.weather.services.WeatherServiceImpl;

@WebMvcTest(WeatherController.class)
public class WeatherControllerWebLayerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private WeatherServiceImpl mockService;
	@MockBean
	private RestTemplate mockRestTemplate;
	
	@Test
	public void testGetForecast_Success() throws Exception {
		LatLong latLong = new LatLong("21.3099", "-157.8581");
		
		when(mockService.getLatitudeAndLongitude("Honolulu", "HI","US"))
			.thenReturn(latLong);

		WeatherForecast forecast = new WeatherForecast(21.3099, -157.8581, "F", 83, 71, "Sunny",
									"Sunny, with a high near 83. East wind around 12 mph.");
		ResponseEntity<WeatherForecast> response = ResponseEntity.ok(forecast);
		String forecastUrl = WeatherController.FORECAST_URL_BASE + latLong.getLatitude() 
																 + "," + latLong.getLongitude();
		when(mockRestTemplate.getForEntity(forecastUrl, WeatherForecast.class))
			.thenReturn(response);

		mockMvc.perform(get("/weather/forecast/US/HI/Honolulu"))
		       .andDo(print())
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.latitude").value("21.3099"))
		       .andExpect(jsonPath("$.longitude").value("-157.8581"))
		       .andExpect(jsonPath("$.temperatureUnit").value("F"))
		       .andExpect(jsonPath("$.highTemperature").value("83"))
		       .andExpect(jsonPath("$.lowTemperature").value("71"))
		       .andExpect(jsonPath("$.forecast").value("Sunny"))
		       .andExpect(jsonPath("$.detailedForecast").value("Sunny, with a high near 83. East wind around 12 mph."));
	}
	
	
	
	@Test
	public void testQueryForWeather_NullJsonReturned() throws Exception {
		when(mockService.getLatitudeAndLongitude("Honolulu", "HI","US"))
			.thenReturn(null);
		
		mockMvc.perform(get("/weather/forecast/US/HI/Honolulu"))
		       //.andDo(print())
		       .andExpect(status().is4xxClientError())
		       .andExpect(content().string(is(emptyOrNullString())));
	}
	
	@Test
	public void testQueryForWeatherDaoException() throws Exception {
		when(mockService.getLatitudeAndLongitude("Honolulu", "HI","US"))
			.thenThrow(new WeatherDatabaseException());
		
		mockMvc.perform(get("/weather/forecast/US/HI/Honolulu"))
		       .andExpect(status().isInternalServerError())
		       .andExpect(content().string(is(emptyOrNullString())));
	}

}
