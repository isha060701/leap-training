package com.fidelity.integration;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.junit.jupiter.api.Assertions.*;

import com.fidelity.model.Employee;

class EmployeeDaoDMLTest {
	private JdbcTemplate jdbcTemplate;
	private DbTestUtils dbTestUtils;
	private EmployeeDao dao;
	private SimpleDataSource dataSource;
	private Connection connection;
	private TransactionManager transactionManager;
	
	private Employee emp7369;
	private Employee emp7934;
	
	  
	@BeforeEach
	void setUp() throws SQLException {
		dataSource = new SimpleDataSource();
		connection = dataSource.getConnection();
		dao=new EmployeeDaoOracleImpl(dataSource);
		
						
		// Start the transaction
		transactionManager=new TransactionManager(dataSource);
		transactionManager.startTransaction();
		
		dbTestUtils = new DbTestUtils(connection);		
		jdbcTemplate = dbTestUtils.initJdbcTemplate();
		
		
		// Note format of date depends on query and/or database parameters
		// NULL commission handled correctly
		emp7369 = new Employee(7369, "SMITH", "CLERK", 7902, LocalDate.of(1980, 12,	17), new BigDecimal("800.00"), null, 20);
		emp7934 = new Employee(7934, "MILLER", "CLERK", 7782, LocalDate.of(1982, 01, 23), new BigDecimal("1300.00"), null, 10);
	}

	@AfterEach
	void tearDown() throws SQLException {
		// Rollback the transaction
		transactionManager.rollbackTransaction();
		
		// Shutdown the DataSource
		dataSource.shutdown();
	}
	
	@Test
	void testInsertEmployee() throws SQLException {
		int oldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "emp");
		assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "emp", "empno = 8000"));
		Employee newEmployee = new Employee(8000, "HEYES", "ANALYST", 7934, LocalDate.of(1980, 12, 17), 
		new BigDecimal("500.00"), null, 10);
		dao.insertEmployee(newEmployee);
		assertEquals(oldSize + 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "emp"));
		assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "emp", "empno = 8000"));
	}
	
	@Test
	void testUpdateEmployee() {
		int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "emp");
		
		String whereCondition="empno= 7369 and ename='Updated' and job='Updated'";
		
		int rowCount=JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "emp",whereCondition);
		assertEquals(0, rowCount);
		
		Employee newEmployee = new Employee(7369, "Updated", "Updated", 7934, LocalDate.of(1980, 12, 17), 
		new BigDecimal("500.00"), null, 10);
		
		dao.updateEmployee(newEmployee);
		
		int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "emp");
		
		assertEquals(expectedRows,actualRows);
		
		rowCount=JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "emp",whereCondition);
		assertEquals(1, rowCount);
	}
	
	@Test
	void testDeleteEmployee(){
		int oldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "emp");
		int id=7369;
		dao.deleteEmployee(id);
		int newSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "emp");
		assertEquals(oldSize-1,newSize);
		int rowsWithDeletedId=JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "emp","empno="+id);
		assertEquals(0,rowsWithDeletedId);
		
	}
	
	@Test
	void retrieveQueryAllEmployeeWithPerfomanceReviewFirstElement() {
		Employee employee1=new Employee(7369,"SMITH","CLERK",7902,LocalDate.of(1980, 12, 17),new BigDecimal(800.00),null,20);
		List<Employee> empList=dao.queryAllEmployeesWithPerformanceReview();
		assertEquals(PerformanceReviewResult.ABOVE,empList.get(0).getReview());
	}
	
	@Test
	void retrieveQueryAllEmployeeWithPerfomanceReviewFirstElementByEmpNo() {
		Employee employee1=new Employee(7369,"SMITH","CLERK",7902,LocalDate.of(1980, 12, 17),new BigDecimal(800.00),null,20);
		Employee emp=dao.queryAllEmployeesWithPerformanceReviewByEmpno(7369);
		System.out.println(emp.getReview());
		assertEquals(PerformanceReviewResult.ABOVE,emp.getReview());
	}

	/***** Utility Methods *****/
	private void beginTransaction() throws SQLException {
		connection.setAutoCommit(false);
	}
	
	private void rollbackTransaction() throws SQLException {
		connection.rollback();	
	}
}
