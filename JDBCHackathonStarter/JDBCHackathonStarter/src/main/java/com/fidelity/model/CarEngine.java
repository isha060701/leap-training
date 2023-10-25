package com.fidelity.model;

import java.util.Objects;

/**
 * CarEngine defines the properties of an engine for one Car's engine.
 * Car has a 1-to-1 relationship with CarEngine, but note that not
 * every car has an engine.
 * 
 * @author ROI Instructor Team
 */
public class CarEngine {
	// You may not change the datatype of any of the properties of CarEngine
	private int carId;
	private CarEngineType engineType;
	private float horsepower;
	private float engineSizeInLiters;

	
	public CarEngine() {}

	public CarEngine(int carId, CarEngineType engineType, float engineSizeInLiters,
			         float horsepower) {
		this.carId = carId;
		this.engineType = engineType;
		this.engineSizeInLiters = engineSizeInLiters;
		this.horsepower = horsepower;
	}
	
	public int getCarId() {
		return carId;
	}

	public CarEngineType getEngineType() {
		return engineType;
	}

	public float getHorsepower() {
		return horsepower;
	}

	public float getEngineSizeInLiters() {
		return engineSizeInLiters;
	}


	@Override
	public int hashCode() {
		return Objects.hash(carId, engineSizeInLiters, engineType, horsepower);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CarEngine other = (CarEngine) obj;
		return carId == other.carId
				&& Float.floatToIntBits(engineSizeInLiters) == Float.floatToIntBits(other.engineSizeInLiters)
				&& engineType == other.engineType
				&& Float.floatToIntBits(horsepower) == Float.floatToIntBits(other.horsepower);
	}
	
	

}
