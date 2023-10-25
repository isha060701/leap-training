package com.fidelity.restcontroller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables;
import static org.springframework.test.jdbc.JdbcTestUtils.dropTables;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.web.util.UriComponentsBuilder;

import com.fidelity.business.Ship;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"},
	 executionPhase = ExecutionPhase.AFTER_TEST_METHOD) 
class ShipControllerE2ETest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Test
	public void testQueryForAllShips() {
		int presidentCount = countRowsInTable(jdbcTemplate, "famousships");
		
		String request = "/ships";

		ResponseEntity<Ship[]> response = 
				restTemplate.getForEntity(request, Ship[].class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		Ship[] responseShips = response.getBody();
		assertEquals(presidentCount, responseShips.length); 
		
	}
	
	@Test
	public void testQueryForAllShips_NoShipsInDb() {
		deleteFromTables(jdbcTemplate, "famousships");
		
		String request = "/ships";

		ResponseEntity<String> response = restTemplate.getForEntity(request, String.class);
		
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		
		assertTrue(response.getBody() == null || response.getBody().isBlank());
	}
	

	@Test
	public void testQueryForAllShips_FirstElement() {
		int id = 1;
		String requestUrl = "/ships";
	    ResponseEntity<Ship[]> response = restTemplate.getForEntity(requestUrl, Ship[].class);
	    
	    Ship[] ships = response.getBody();
	    assertEquals(id, ships[0].getId());
	}
	
	@Test
	void getShipByIdOkStatus() {
		int id = 1;
		String requestUrl = UriComponentsBuilder.fromPath("/ships/id/{id}")
		            .buildAndExpand(id)
		            .toUriString();
		
		ResponseEntity<Ship> response = restTemplate.getForEntity(requestUrl, Ship.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	void getShipByIdListSize() {
		
		int id = 1;
		String requestUrl = UriComponentsBuilder.fromPath("/ships/id/{id}")
		            .buildAndExpand(id)
		            .toUriString();
		ResponseEntity<Ship> response = restTemplate.getForEntity(requestUrl, Ship.class);
		
		Ship responseShip = response.getBody();
		assertEquals(id, responseShip.getId()); 
	}
	
	@Test
	public void testQueryForShipNotInDb_NoShipsInDb() {
		int id = 99;
		String requestUrl = UriComponentsBuilder.fromPath("/ships/id/{id}")
		            .buildAndExpand(id)
		            .toUriString();
		ResponseEntity<Ship> response = restTemplate.getForEntity(requestUrl, Ship.class);
		
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
	
	@Test
	public void testQueryForShipNotInDb_InvalidId() {
		int id = -1;
		String requestUrl = UriComponentsBuilder.fromPath("/ships/id/{id}")
		            .buildAndExpand(id)
		            .toUriString();
		ResponseEntity<Ship> response = restTemplate.getForEntity(requestUrl, Ship.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void testQueryGetCaptainByNameStatus() {
		String name = "RMS Titanic";
		String requestUrl = UriComponentsBuilder.fromPath("/ships/name/{name}")
	            .buildAndExpand(name)
	            .toUriString();
		
		ResponseEntity<ShipCaptainResponse> response = restTemplate.getForEntity(requestUrl, ShipCaptainResponse.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void testQueryGetCaptainByNameNotInDB() {
		String name = "RMS";
		String requestUrl = UriComponentsBuilder.fromPath("/ships/name/{name}")
	            .buildAndExpand(name)
	            .toUriString();
		
		ResponseEntity<ShipCaptainResponse> response = restTemplate.getForEntity(requestUrl, ShipCaptainResponse.class);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
	
	@Test
	public void testQueryGetCaptainByName() {
		String name = "RMS Titanic";
		String requestUrl = UriComponentsBuilder.fromPath("/ships/name/{name}")
	            .buildAndExpand(name)
	            .toUriString();
		
		ResponseEntity<ShipCaptainResponse> response = restTemplate.getForEntity(requestUrl, ShipCaptainResponse.class);
		
		assertEquals(new ShipCaptainResponse("Captain Edward J. Smith"), response.getBody());
	}

}
