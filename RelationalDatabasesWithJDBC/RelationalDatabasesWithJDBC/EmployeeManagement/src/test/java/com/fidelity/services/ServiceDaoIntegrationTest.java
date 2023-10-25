package com.fidelity.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fidelity.integration.DbTestUtils;
import com.fidelity.integration.EmployeeDao;
import com.fidelity.integration.SimpleDataSource;
import com.fidelity.model.Employee;

/*
 * The tests in this file are integration tests, not unit tests.
 * The tests have a striking resemblance to the tests in EmployeeManagementServiceTest.
 * But in these tests, the EmployeeManagementService is using the EmployeeDaoOracleImpl
 * not a MockDao.
 * To prevent modifying the Oracle database, a transaction is started before each test
 * and the transaction is rolled back after each test.
 */
class ServiceDaoIntegrationTest {
	private JdbcTemplate jdbcTemplate;
	private DbTestUtils dbTestUtils;
	private EmployeeManagementService service;
	private EmployeeDao dao;
	private SimpleDataSource dataSource;
	private Connection connection;
	private Employee emp7369;
	private Employee emp7934;
	
	@BeforeEach
	void setUp() throws Exception {
		dataSource = new SimpleDataSource();
		
		connection = dataSource.getConnection();
		
		dbTestUtils = new DbTestUtils(connection);
		jdbcTemplate = dbTestUtils.initJdbcTemplate();
	}

	@AfterEach
	void tearDown() throws Exception {
		dataSource.shutdown();
	}

}
