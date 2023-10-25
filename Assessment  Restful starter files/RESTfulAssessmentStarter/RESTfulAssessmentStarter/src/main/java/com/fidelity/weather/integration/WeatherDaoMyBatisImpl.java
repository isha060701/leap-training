package com.fidelity.weather.integration;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fidelity.weather.integration.mapper.WeatherMapper;
import com.fidelity.weather.models.LatLong;

@Repository
public class WeatherDaoMyBatisImpl implements WeatherDao {

	@Autowired
	private Logger logger;
	
	@Autowired
	private WeatherMapper mapper;
	
	@Override
	public LatLong getLatitudeAndLongitude(String city, String state,String country) {

		if (city == null || city.isBlank()) {
			throw new IllegalArgumentException("city may not be empty");
		}
		if (state == null || state.isBlank()) {
			throw new IllegalArgumentException("state may not be empty");
		}
		if (country == null || country.isBlank()) {
			throw new IllegalArgumentException("state may not be empty");
		}
		return mapper.getLatitudeAndLongitude(city, state,country);
	}

}
