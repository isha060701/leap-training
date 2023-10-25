package com.fidelity.weather.integration.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fidelity.weather.models.LatLong;

@Mapper
public interface WeatherMapper {
	LatLong getLatitudeAndLongitude(@Param("city") String city, 
									@Param("state") String state,
									@Param("country") String country);
}
