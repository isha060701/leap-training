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

	public abstract List<Employee> queryEmployeeByName(String name);

}
