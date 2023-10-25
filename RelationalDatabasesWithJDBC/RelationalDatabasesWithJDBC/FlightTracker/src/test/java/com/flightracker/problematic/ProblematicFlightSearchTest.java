package com.flightracker.problematic;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.flighttracker.jdbc.FlightSearch;
import com.flighttracker.model.Flight;

class ProblematicFlightSearchTest {
	private FlightSearch searcher;
	private DataSource dataSource;
	private Connection connection;
	
	@BeforeEach
	void setUp() throws Exception {
		dataSource = new ProblematicDataSource();
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
	@Disabled
	void testFindFlights() throws SQLException {
		List<Flight> flights = searcher.findFlights("Chicago", "New York");
		assertNotNull(flights);
		assertTrue(flights.size() > 0);
	}

}
