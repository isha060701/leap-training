package com.fidelity.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.fidelity.model.Employee;

class EmployeeDaoQueryTest {
	private EmployeeDao dao;
	private SimpleDataSource dataSource;
	
	private Employee emp7369;
	private Employee emp7934;
	  
	@BeforeEach
	void setUp() throws SQLException {
		dataSource = new SimpleDataSource();
		
		dao = new EmployeeDaoOracleImpl(dataSource);
		
		// Note format of date depends on query and/or database parameters
		// NULL commission handled correctly
		emp7369 = new Employee(7369, "SMITH", "CLERK", 7902, "17-DEC-1980", new BigDecimal("800.00"), null, 20);
		emp7934 = new Employee(7934, "MILLER", "CLERK", 7782, "23-JAN-1982", new BigDecimal("1300.00"), null, 10);
	}

	@AfterEach
	void tearDown() throws SQLException {
		dataSource.shutdown();
	}

	/***** Query Tests *****/
	@Test
	void testQueryAllEmployees() {
		List<Employee> emps = dao.queryAllEmployees();
		assertEquals(14, emps.size(), "Should be 14 records");
		assertTrue(emps.contains(emp7369), "Should contain SMITH 7369");
		assertTrue(emps.contains(emp7934), "Should contain MILLER 7934");
	}
		
	@ParameterizedTest
	@ValueSource(strings = { "SMITH", "MILLER" })
	void testQueryEmployeeByName(String name) {
		List<Employee> employees = dao.queryEmployeeByName(name);
		assertNotNull(employees);
		for (Employee employee : employees) {
			assertEquals(employee.getEmpName(), name);
		}
	}
	
	@ParameterizedTest
	@ValueSource(ints = {7369, 7499, 7839})
	void testQueryEmployeeById(int id) {
		Employee emp = dao.queryEmployeeById(id);
		assertEquals(id, emp.getEmpNumber());
	}
	
}
