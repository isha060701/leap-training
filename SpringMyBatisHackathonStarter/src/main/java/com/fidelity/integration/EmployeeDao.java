package com.fidelity.integration;

import java.util.List;

import com.fidelity.business.Employee;

public interface EmployeeDao {
	
	/* DO NOT MODIFY  */
	
	List<Employee> getAllEmployees();
	List<Employee> getAllEmployeesAndJobs();
	//implementation should throw a custom RuntimeException if an Employee
	//can't be added
	void add(Employee emp);


}
