package com.fidelity.model;

/**
 * CarBodyType describes the type of a vehicle's body.
 *
 * @author ROI Instructor Team
 */
public enum CarBodyType {
	SEDAN(1, "Sedan"), 
	TRUCK(2, "Truck"), 
	CONVERTIBLE(3, "Convertible"), 
	OTHER(4, "Other");

	private int code;
	private String name;

	private CarBodyType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public static CarBodyType of(int code) {
		for (CarBodyType bodyType : values()) {
			if (bodyType.code == code) {
				return bodyType;
			}
		}
		throw new IllegalArgumentException("Unknown body type code " + code);
	}

	public static CarBodyType of(String name) {
		for (CarBodyType bodyType : values()) {
			if (name.equals(bodyType.name)) {
				return bodyType;
			}
		}
		throw new IllegalArgumentException("Unknown body type name " + name);
	}

	public int getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
}
