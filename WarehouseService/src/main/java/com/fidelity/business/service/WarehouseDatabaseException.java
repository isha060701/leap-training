package com.fidelity.business.service;

public class WarehouseDatabaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public WarehouseDatabaseException() {
		super();
	}

	public WarehouseDatabaseException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public WarehouseDatabaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public WarehouseDatabaseException(String message) {
		super(message);
	}

	public WarehouseDatabaseException(Throwable cause) {
		super(cause);
	}

}
