package com.flighttracker.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flighttracker.model.Flight;

public class FlightSearch {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private DataSource datasource;
	private String sqlFindFlightsDestination = """
		select departure, destination, id, origin
		 from  flight
        where  destination = 
	""";
	private String sqlFindFlightsOrigin = "	and origin = ";

	public FlightSearch(DataSource datasource) {
		this.datasource = datasource;
	}
	
	public List<Flight> findFlights(String origin, String destination) throws SQLException {
		List<Flight> flights = new ArrayList<>();
		String sql =  sqlFindFlightsDestination 
					  + "'" + destination + "'" 
					  + sqlFindFlightsOrigin 
					  + "'" + origin + "'";
		Statement stmt = null;
		Connection conn = null;
		
		try {
			conn = datasource.getConnection();
			stmt = conn.createStatement();
			
			// execute the flight lookup query
			ResultSet rs = stmt.executeQuery(sql);
			
			// assign the collection to flights
			flights = processFlightRecords(rs);
		} finally {
			if (stmt != null) {
				logger.debug("Closing the JDBC Statement");
				stmt.close();
			}
			
			if (conn != null) {
				logger.debug("Closing the JDBC Connection");
				conn.close();
			}
		}
		
		return flights;
	}

	private List<Flight> processFlightRecords(ResultSet rs) {
		List<Flight> flights = new ArrayList<>();
		
		try {
			while (rs.next() ) {
				String origin = rs.getString("origin");
				String destination = rs.getString("destination");
				LocalDateTime departureDate = rs.getTimestamp("departure")
												.toLocalDateTime();
				int id = rs.getInt("id");
				
				Flight flight = new Flight(id, origin, destination, departureDate);
				
				flights.add(flight);
			}
		} catch (SQLException e) {
			throw new DataAccessException("Unable to process flight records", e);
		}
		
		return flights;
	}
}
