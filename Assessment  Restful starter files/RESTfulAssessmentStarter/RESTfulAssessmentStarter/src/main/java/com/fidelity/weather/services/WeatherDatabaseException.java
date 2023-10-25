package com.fidelity.weather.services;

public class WeatherDatabaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public WeatherDatabaseException() {
		super();
	}

	public WeatherDatabaseException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public WeatherDatabaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public WeatherDatabaseException(String message) {
		super(message);
	}

	public WeatherDatabaseException(Throwable cause) {
		super(cause);
	}

}