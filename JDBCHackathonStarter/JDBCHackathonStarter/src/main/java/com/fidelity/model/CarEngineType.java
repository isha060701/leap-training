/**
 * 
 */
package com.fidelity.model;

/**
 * CarEngineType describe the type of a vehicle's engine.
 * 
 * @author ROI Instructor Team
 */
public enum CarEngineType {
	GASOLINE(1),
	DIESEL(2),
	ELECTRIC(3),
	HYBRID(4),
	ALTERNATIVE(5);

	private int code;
	
	/**
	 * @param type the engine type
	 */
	private CarEngineType(int code) {
		this.code = code;
	}
	
	public static CarEngineType of(int code) {
		for (CarEngineType engineType : values()) {
			if (engineType.code == code) {
				return engineType;
			}
		}
		throw new IllegalArgumentException("Unknown body type code " + code);
	}

	public int getCode() {
		return code;
	}
}
