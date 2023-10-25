package com.fidelity.integration;
/*
 * DbTestUtils.java - utility functions for database integration tests.
 */

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;


public class DbTestUtils
{

	private static final String SQL_SCRIPT = "sql/emp_populate.sql";

	private Connection connection;

	public DbTestUtils(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Re-run the database initialization script.
	 * Only pure SQL scripts allowed - PL/SQL statements must be removed
	 * 
	 */
	public void initDb() {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		
		populator.setContinueOnError(true);
		populator.addScript(new FileSystemResource(SQL_SCRIPT));
		populator.populate(connection);
	}

	public JdbcTemplate initJdbcTemplate() {
		return new JdbcTemplate(new SingleConnectionDataSource(connection, true));
	}

	private int intValue(Object bigDecimal) {
		return bigDecimal != null ? ((BigDecimal) bigDecimal).intValue() : 0;
	}

	private double doubleValue(Object bigDecimal) {
		return bigDecimal != null ? ((BigDecimal) bigDecimal).doubleValue() : 0;
	}

	private String formatTimestamp(Object timestamp) {
		Date date = new Date(((Timestamp) timestamp).getTime());
		return new SimpleDateFormat("dd-MMM-yyyy").format(date).toUpperCase();
	}

}
