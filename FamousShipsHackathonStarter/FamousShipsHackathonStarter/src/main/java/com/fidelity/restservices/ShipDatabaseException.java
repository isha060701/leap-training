package com.fidelity.restservices;

public class ShipDatabaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ShipDatabaseException() {
		super();
	}

	public ShipDatabaseException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ShipDatabaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public ShipDatabaseException(String message) {
		super(message);
	}

	public ShipDatabaseException(Throwable cause) {
		super(cause);
	}

}
