package com.fidelity.india.secondary.services;

public class SeaDatabaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SeaDatabaseException() {
		super();
	}

	public SeaDatabaseException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SeaDatabaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public SeaDatabaseException(String message) {
		super(message);
	}

	public SeaDatabaseException(Throwable cause) {
		super(cause);
	}

}