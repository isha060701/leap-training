package com.fidelity.weather.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.fidelity.weather.models.LatLong;

@SpringBootTest
@Transactional
class MyBatisWeatherDaoIntegrationTest {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private WeatherDao dao;
	
	@Test
	public void getLatitudeAndLongitude_Success() {
		LatLong expectedLatLong = new LatLong("21.3099", "-157.8581");
		
		LatLong actualLatLong = dao.getLatitudeAndLongitude("Honolulu", "HI","US");
		
		assertEquals(expectedLatLong, actualLatLong);
	}
	
	@Test
	public void getLatitudeAndLongitude_NotFound() {
		
		LatLong actualLatLong = dao.getLatitudeAndLongitude("Nowhere", "NY","US");
		
		assertNull(actualLatLong);
	}
	
	@Test
	public void getLatitudeAndLongitude_CityNullThrowsException() {
		
		
		
		assertThrows(IllegalArgumentException.class, ()->{
			dao.getLatitudeAndLongitude(null, "NY","US");
		});
	}
	
	@Test
	public void getLatitudeAndLongitude_StateNullThrowsException() {
		
		
		
		assertThrows(IllegalArgumentException.class, ()->{
			dao.getLatitudeAndLongitude("NY",null,"US");
		});
	}
	
	@Test
	public void getLatitudeAndLongitude_CountyNullThrowsException() {
		
		
		
		assertThrows(IllegalArgumentException.class, ()->{
			dao.getLatitudeAndLongitude("NY","US",null);
		});
	}

}
