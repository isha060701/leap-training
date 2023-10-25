package com.fidelity.weather.services;

import com.fidelity.weather.models.LatLong;

public interface WeatherService {
	LatLong getLatitudeAndLongitude(String city, String state,String countrty);

}
