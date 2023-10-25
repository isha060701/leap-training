package com.fidelity.integration;

import java.util.List;

import com.fidelity.model.Car;

/**
 * CarDao defines the methods for all Car DAO implementations.
 * 
 * @author ROI Instructor Team
 */
public interface CarDao {
	// You may not make any changes to the methods
	
	List<Car> getCars();
	void insertCar(Car car);
	void updateCar(Car car);
	void deleteCar(int carId);
}
