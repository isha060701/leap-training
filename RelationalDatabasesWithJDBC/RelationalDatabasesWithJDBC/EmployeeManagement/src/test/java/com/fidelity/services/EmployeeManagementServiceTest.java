package com.fidelity.services;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.fidelity.integration.EmployeeDao;
import com.fidelity.model.Employee;

class EmployeeManagementServiceTest {
	private EmployeeManagementService service;
	private EmployeeDao dao;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	
}

class MockDao {
	private List<Employee> employees;
	
	MockDao(DataSource ds) {
		employees = new ArrayList<>();
		
		employees.add(new Employee(7369, "SMITH", "CLERK", 7902, LocalDate.of(1980, 12,	17), new BigDecimal("800.00"), null, 20));
		employees.add(new Employee(7934, "MILLER", "CLERK", 7782, LocalDate.of(1982, 01, 23), new BigDecimal("1300.00"), null, 10));
	}
	
	public List<Employee> queryAllEmployees() {
		return employees;
	}

	public Employee queryEmployeeById(int empNo) {
		Employee employee = null;
		
		for (Employee emp : employees) {
			if (emp.getEmpNumber() == empNo) {
				employee = emp;
				break;
			}
		}
		return employee;
	}


}
