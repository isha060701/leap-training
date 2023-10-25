package com.fidelity.weather.services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fidelity.weather.integration.WeatherDaoMyBatisImpl;
import com.fidelity.weather.models.LatLong;

@Service
@Transactional
public class WeatherServiceImpl implements WeatherService {
	
	@Autowired
	private Logger logger;
	
	@Autowired
	private WeatherDaoMyBatisImpl dao;

	@Override
	public LatLong getLatitudeAndLongitude(String city, String state,String country) {
		// TODO Auto-generated method stub
		LatLong latLong;
		if (city == null || city.isBlank()) {
			throw new IllegalArgumentException("city cannot be empty");
		}
		if (state == null || state.isBlank()) {
			throw new IllegalArgumentException("state cannot be empty");
		}
		if (country == null || country.isBlank()) {
			throw new IllegalArgumentException("state cannot be empty");
		}
		try {
			latLong = dao.getLatitudeAndLongitude(city,state,country);
		} 
		catch (DataAccessException e) {
			String msg = "Error getting latitude and longitude from the database.";
			logger.error(msg,e);
			throw new WeatherDatabaseException(msg, e);
		}

		return latLong;
	}

}
