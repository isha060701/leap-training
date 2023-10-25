package com.roifmr.presidents.service;

public class PresidentsDatabaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PresidentsDatabaseException() {
		super();
	}

	public PresidentsDatabaseException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PresidentsDatabaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public PresidentsDatabaseException(String message) {
		super(message);
	}

	public PresidentsDatabaseException(Throwable cause) {
		super(cause);
	}

}
