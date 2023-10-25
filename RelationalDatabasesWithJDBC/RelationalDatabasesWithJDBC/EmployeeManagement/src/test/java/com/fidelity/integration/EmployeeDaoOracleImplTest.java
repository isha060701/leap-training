package com.fidelity.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fidelity.model.Employee;

class EmployeeDaoOracleImplTest {
	private SimpleDataSource dataSource;
	private EmployeeDao employeeDaoOracleImpl;

	@BeforeEach
	void setUp() throws Exception {
		dataSource=new SimpleDataSource();
		employeeDaoOracleImpl=new EmployeeDaoOracleImpl(dataSource);
	}

	@AfterEach
	void tearDown() throws Exception {
		dataSource.shutdown();
	}
	
	@Test
	void canMakeEmployeeDaoOracleImplWithDataSourceConstructor() {
		assertNotNull(employeeDaoOracleImpl);
	}
	
	@Test
	void retrieveQueryAllEmployeeListSize()
	{
		List<Employee> emps = employeeDaoOracleImpl.queryAllEmployees();
		assertEquals(14, emps.size());
	}
	
	@Test
	void retrieveQueryAllEmployeeFirstElement() {
		Employee employee1=new Employee(7369,"SMITH","CLERK",7902,LocalDate.of(1980, 12, 17),new BigDecimal(800.00),null,20);
		List<Employee> empList=employeeDaoOracleImpl.queryAllEmployees();
		assertEquals(employee1,empList.get(0));
	}
	
	@Test
	void retrieveQueryEmployeeById() {
		Employee employee1=new Employee(7369,"SMITH","CLERK",7902,LocalDate.of(1980, 12, 17),new BigDecimal(800.00),null,20);
		Employee actual=employeeDaoOracleImpl.queryEmployeeById(7369);
		assertEquals(employee1,actual);
	}
	
	@Test
	void queryEmployeeByIdWhenIdIsNotPresent()
	{
		assertNull(employeeDaoOracleImpl.queryEmployeeById(1000));
	}
	
	
	

}
