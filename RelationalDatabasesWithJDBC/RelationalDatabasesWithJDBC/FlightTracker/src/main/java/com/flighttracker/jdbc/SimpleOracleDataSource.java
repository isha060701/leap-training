package com.flighttracker.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Objects;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the Oracle implementation of the SimpleDataSource interface.
 * The constructors read a properties file that should define the 
 * properties necessary to connect to an Oracle database.
 * 
 * The SimpleOracleDataSource retains a reference to the Connection
 * it obtains in the getConnection() method.
 * 
 * A new Connection will be opened if the current Connection is either 
 * null or closed. 
 * 
 * @author ROI Instructor
 *
 */
public class SimpleOracleDataSource implements DataSource {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final static String dbPropertiesFile = "db.properties";
	private Properties properties;
	private Connection connection;

	public SimpleOracleDataSource() throws IOException {
		this(dbPropertiesFile);
	}
	
	public SimpleOracleDataSource(String path) throws IOException {
		properties = readPropertiesFile(path);
	}

	public Connection getConnection() {
		try {
			if (Objects.isNull(connection) || connection.isClosed()) {
				String dbUrl = properties.getProperty("db.url");
				String userName = properties.getProperty("db.username");
				String password = properties.getProperty("db.password");

				connection = DriverManager.getConnection(dbUrl, userName, password);
				logger.info("Received Connection from DriverManager for " + dbUrl);
			}
		} catch (SQLException ex) {
			throw new DataAccessException("Unable to open Oracle connection", ex);
		}
		
		return connection;
	}
	
	private Properties readPropertiesFile(String path) throws IOException {
		Properties properties = new Properties();
		properties.load(SimpleOracleDataSource.class
											  .getClassLoader()
											  .getResourceAsStream(path));

		return properties;
	}

	//***** Unused methods declared in the DataSource interface
	@Override
	public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return null;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

}
