/*
 * DbTestUtils.java - utility functions for database integration tests.
 */

package com.roifmr.presidents.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

public enum DbTestUtils {
	INSTANCE;
	
	private static final String DB_PROPERTIES_FILE = "application-prod.properties";
	
	private static final String DB_URL_PROPERTY = "spring.datasource.url";
	private static final String DB_USERNAME_PROPERTY = "spring.datasource.username";
	private static final String DB_PASSWORD_PROPERTY = "spring.datasource.password";

	private static final String SCHEMA_SQL = "schema.sql";
	private static final String DATA_SQL = "data.sql";

	/**
	 * Re-run the database initialization scripts.
 	 * This may be necessary for some test classes. For example, you can call
 	 * initDb() from an @AfterAll method:
	 * 		@AfterAll
	 * 		public static void restoreDatabase() {
	 * 			DbTestUtils.INSTANCE.initDb();
	 * 		}
	 */
	public void initDb() {
		Connection connection = null;
		try {
			Properties dbProps = new Properties();
			dbProps.load(getClass().getClassLoader().getResourceAsStream(DB_PROPERTIES_FILE));
			connection = DriverManager.getConnection(dbProps.getProperty(DB_URL_PROPERTY), 
													 dbProps.getProperty(DB_USERNAME_PROPERTY), 
													 dbProps.getProperty(DB_PASSWORD_PROPERTY));

			// Use Spring utility classes to run the DB scripts
			// Only pure SQL scripts allowed - PL/SQL statements must be removed 
			ResourceDatabasePopulator resourceDatabasePopulator = 
					new ResourceDatabasePopulator(true, true, null, 
							new ClassPathResource(SCHEMA_SQL), new ClassPathResource(DATA_SQL));
			resourceDatabasePopulator.populate(connection);
			Thread.sleep(500);
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			close(connection);
		}
	}

	private synchronized void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} 
			catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
