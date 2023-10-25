package com.flighttracker.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.flighttracker.model.Flight;

class FlightSearchTest {
	private FlightSearch searcher;
	private DataSource dataSource;
	private Connection connection;
	
	@BeforeEach
	void setUp() throws Exception {
		dataSource = new SimpleOracleDataSource();
		searcher = new FlightSearch(dataSource);
		connection = dataSource.getConnection();
	}

	@AfterEach
	void tearDown() throws Exception {
		if(Objects.nonNull(connection)) {
			connection.close();
		}
	}

	@Test
	void testFlightSearchCreated() {
		assertNotNull(searcher);
	}

	@Test
	void testFindFlights() throws SQLException {
		String origin = "Chicago";
		String destination = "New York";
		List<Flight> flights = searcher.findFlights(origin, destination);
		assertNotNull(flights);
		assertTrue(flights.size() > 0);
		for (Flight flight : flights) {
			assertTrue(origin.equals(flight.getOrigin()));
			assertTrue(destination.equals(flight.getDestination()));
		}
	}

}
