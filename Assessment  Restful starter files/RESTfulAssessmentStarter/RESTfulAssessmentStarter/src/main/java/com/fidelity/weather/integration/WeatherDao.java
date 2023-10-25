package com.fidelity.weather.integration;

import com.fidelity.weather.models.LatLong;

public interface WeatherDao {
	LatLong getLatitudeAndLongitude(String city, String state,String country);

}
