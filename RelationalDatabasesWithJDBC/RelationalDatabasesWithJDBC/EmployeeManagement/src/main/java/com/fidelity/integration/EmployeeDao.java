package com.fidelity.integration;

import java.util.List;

import javax.sql.DataSource;

import com.fidelity.model.Employee;

public abstract class EmployeeDao {
	private DataSource dataSource;
	
	public EmployeeDao(DataSource datasource) {
		this.dataSource = datasource;
	}
	
	protected DataSource getDataSource() {
		return dataSource;
	}

	public abstract List<Employee> queryAllEmployees();

	public abstract Employee queryEmployeeById(int empNo);
	
	public abstract void insertEmployee(Employee emp);
	
	public abstract Employee updateEmployee(Employee emp);
	
	public abstract void deleteEmployee(int empNo);

	protected abstract List<Employee> queryAllEmployeesWithPerformanceReview();

	protected abstract Employee queryAllEmployeesWithPerformanceReviewByEmpno(int i);

}
