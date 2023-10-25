package com.roifmr.presidents.restcontroller;

import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.roifmr.presidents.models.President;



@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"},
	 executionPhase = ExecutionPhase.AFTER_TEST_METHOD) 
public class PresidentsControllerE2eTest {
	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private JdbcTemplate jdbcTemplate;  
	
	private President president1 = new President(1,"George","Washington",1789,1797,"georgewashington.jpg",
			"On April 30, 1789, George Washington, standing on the balcony of Federal Hall on Wall Street "
			+ "in New York, took his oath of office as the first President of the United States. "
			+ "\"As the first of every thing, in our situation will serve to establish a Precedent,\" "
			+ "he wrote James Madison, \"it is devoutly wished on my part, that these precedents may be "
			+ "fixed on true principles.\" Born in 1732 into a Virginia planter family, he learned the "
			+ "morals, manners, and body of knowledge requisite for an 18th century Virginia gentleman.");
	
	private President president10 = new President(10,"John","Tyler",1841,1845,"johntyler.jpg",
			"Dubbed \"His Accidency\" by his detractors, John Tyler was the first Vice President to be "
			+ "elevated to the office of President by the death of his predecessor. Born in Virginia in "
			+ "1790, he was raised believing that the Constitution must be strictly construed. He never "
			+ "wavered from this conviction. He attended the College of William and Mary and studied law.");

	
	@Test
	public void testQueryForAllPresidents() {
		int presidentCount = countRowsInTable(jdbcTemplate, "presidents");
		
		String request = "/presidents";

		ResponseEntity<President[]> response = 
				restTemplate.getForEntity(request, President[].class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		President[] responsePresidents = response.getBody();
		assertEquals(presidentCount, responsePresidents.length); 
		
		assertEquals(president1, responsePresidents[0]);
		assertEquals(president10, responsePresidents[9]);
	}

	
	@Test
	public void testQueryForAllPresidents_NoPresidentsInDb() {
		deleteFromTables(jdbcTemplate, "presidents");
		
		String request = "/presidents";

		ResponseEntity<String> response = restTemplate.getForEntity(request, String.class);
		
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		
		assertTrue(response.getBody() == null || response.getBody().isBlank());
	}

	
	@Test
	public void testQueryForPresidentById() {
		String request = "/presidents/10";

		ResponseEntity<BiographyDto> response = 
				restTemplate.getForEntity(request, BiographyDto.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		assertEquals(president10.getBiography(), response.getBody().getBio());
	}

	
	@Test
	public void testQueryForPresidentById_NotPresent() {
		String request = "/presidents/99";

		ResponseEntity<BiographyDto> response = 
				restTemplate.getForEntity(request, BiographyDto.class);
		
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

}
