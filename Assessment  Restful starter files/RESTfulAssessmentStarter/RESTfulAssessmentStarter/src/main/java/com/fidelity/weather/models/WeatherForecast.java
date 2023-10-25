package com.fidelity.weather.models;

import java.util.Objects;

public class WeatherForecast {
	private double latitude;
	private double longitude;
	private String temperatureUnit;
	private int highTemperature;
	private int lowTemperature;
	private String forecast;
	private String detailedForecast;
	
	public WeatherForecast() {}

	public WeatherForecast(double latitude, double longitude, String temperatureUnit, int highTemperature,
			int lowTemperature, String forecast, String detailedForecast) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.temperatureUnit = temperatureUnit;
		this.highTemperature = highTemperature;
		this.lowTemperature = lowTemperature;
		this.forecast = forecast;
		this.detailedForecast = detailedForecast;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getTemperatureUnit() {
		return temperatureUnit;
	}

	public void setTemperatureUnit(String temperatureUnit) {
		this.temperatureUnit = temperatureUnit;
	}

	public int getHighTemperature() {
		return highTemperature;
	}

	public void setHighTemperature(int highTemperature) {
		this.highTemperature = highTemperature;
	}

	public int getLowTemperature() {
		return lowTemperature;
	}

	public void setLowTemperature(int lowTemperature) {
		this.lowTemperature = lowTemperature;
	}

	public String getForecast() {
		return forecast;
	}

	public void setForecast(String forecast) {
		this.forecast = forecast;
	}

	public String getDetailedForecast() {
		return detailedForecast;
	}

	public void setDetailedForecast(String detailedForecast) {
		this.detailedForecast = detailedForecast;
	}

	@Override
	public int hashCode() {
		return Objects.hash(detailedForecast, forecast, highTemperature, latitude, longitude, lowTemperature,
				temperatureUnit);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WeatherForecast other = (WeatherForecast) obj;
		return Objects.equals(detailedForecast, other.detailedForecast) && Objects.equals(forecast, other.forecast)
				&& highTemperature == other.highTemperature
				&& Double.doubleToLongBits(latitude) == Double.doubleToLongBits(other.latitude)
				&& Double.doubleToLongBits(longitude) == Double.doubleToLongBits(other.longitude)
				&& lowTemperature == other.lowTemperature && Objects.equals(temperatureUnit, other.temperatureUnit);
	}

	@Override
	public String toString() {
		return "WeatherForecast [latitude=" + latitude + ", longitude=" + longitude + ", temperatureUnit="
				+ temperatureUnit + ", highTemperature=" + highTemperature + ", lowTemperature=" + lowTemperature
				+ ", forecast=" + forecast + ", detailedForecast=" + detailedForecast + "]";
	}

	
}
