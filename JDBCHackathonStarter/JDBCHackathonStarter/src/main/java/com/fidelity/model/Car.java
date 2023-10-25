package com.fidelity.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Car defines the properties of a vehicle.
 * 
 * @author ROI Instructor Team
 */
public class Car {
	// You may not change the datatype of any of the properties of Car
	private int id;
	private String make;
	private String model;
	private String bodyTypeName;
	private CarEngine engine;
	private LocalDate manufactureDate;

	public Car() {}

	public Car(int id, String make, String model, String bodyTypeName, CarEngine engine, 
			   LocalDate manufactureDate) {
		super();
		this.id = id;
		this.make = make;
		this.model = model;
		this.bodyTypeName = bodyTypeName;
		this.engine = engine;
		this.manufactureDate = manufactureDate;
	}

	public int getId() {
		return id;
	}

	public String getMake() {
		return make;
	}

	public String getModel() {
		return model;
	}

	public String getBodyTypeName() {
		return bodyTypeName;
	}

	public CarEngine getEngine() {
		return engine;
	}

	public LocalDate getManufactureDate() {
		return manufactureDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(bodyTypeName, engine, id, make, manufactureDate, model);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Car other = (Car) obj;
		return Objects.equals(bodyTypeName, other.bodyTypeName) && Objects.equals(engine, other.engine)
				&& id == other.id && Objects.equals(make, other.make)
				&& Objects.equals(manufactureDate, other.manufactureDate) && Objects.equals(model, other.model);
	}
	
	
	

}
