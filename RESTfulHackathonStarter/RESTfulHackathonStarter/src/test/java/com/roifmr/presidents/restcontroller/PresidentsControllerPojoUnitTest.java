package com.roifmr.presidents.restcontroller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerErrorException;
import org.springframework.web.server.ServerWebInputException;

import com.roifmr.presidents.models.President;
import com.roifmr.presidents.service.PresidentsService;
import com.roifmr.presidents.integration.PresidentsDao;


public class PresidentsControllerPojoUnitTest {
	@Mock
	private PresidentsService service;
	@Mock
	private Logger mockLogger;
	
	@InjectMocks
	private PresidentsController controller;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
	}
	
	private static final List<President> expectedPresidents = Arrays.asList(
		new President(1, "George", "Washington", 1789, 1797, "georgewashington.jpg", "Chopped down a cherry tree"), 
		new President(2, "John", "Adams", 1797, 1801, "johnadams.jpg", "Learned and thoughtful")
	);
	
	@Test
	public void testGetAllPresidentsSuccess() throws Exception {
		when(service.queryForAllPresidents())
			.thenReturn(expectedPresidents);
		
		ResponseEntity<List<President>> responseStatus = controller.queryForAllPresidents();
		
		HttpStatus statusCode = responseStatus.getStatusCode();
		assertEquals(HttpStatus.OK, statusCode);
		List<President> actualPresidents = responseStatus.getBody();
		assertEquals(expectedPresidents, actualPresidents);
	}

	@Test
	public void testGetAllPresidentsDaoReturnsEmptyList() throws Exception {
		when(service.queryForAllPresidents())
			.thenReturn(new ArrayList<President>());
		
		ResponseEntity<List<President>> responseStatus = controller.queryForAllPresidents();
		
		HttpStatus statusCode = responseStatus.getStatusCode();
		assertEquals(HttpStatus.NO_CONTENT, statusCode);
		List<President> actualPresidents = responseStatus.getBody();
		assertNull(actualPresidents);
	}

	@Test
	public void testGetAllPresidentsDaoReturnsNull() throws Exception {
		when(service.queryForAllPresidents())
			.thenReturn(null);
		
		ResponseEntity<List<President>> responseStatus = controller.queryForAllPresidents();
		
		HttpStatus statusCode = responseStatus.getStatusCode();
		assertEquals(HttpStatus.NO_CONTENT, statusCode);
		List<President> actualPresidents = responseStatus.getBody();
		assertNull(actualPresidents);
	}

	@Test
	public void testGetAllPresidentsDaoThrowsException() throws Exception {
		when(service.queryForAllPresidents())
			.thenThrow(new RuntimeException("mock exception"));
		
		ServerErrorException ex = assertThrows(ServerErrorException.class, 
			() -> controller.queryForAllPresidents()
		);
			
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
	}

	@Test
	public void testQueryForPresidentBiographySuccess() throws Exception {
		String expectedBio = expectedPresidents.get(0).getBiography();
		when(service.queryForPresidentBiography(anyInt()))
			.thenReturn(expectedBio);
				
		ResponseEntity<BiographyDto> response = controller.queryForPresidentBiography(1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedBio, response.getBody().getBio());
	}
	
	@Test
	public void testQueryForPresidentBiographyNonExistantId() throws Exception {
		when(service.queryForPresidentBiography(anyInt()))
			.thenReturn(null);
				
		ResponseEntity<BiographyDto> response = controller.queryForPresidentBiography(99);
		
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		assertNull(response.getBody());
	}
	
	@Test
	public void testQueryForPresidentBiographyInvalidId() throws Exception {
		ServerWebInputException ex = assertThrows(ServerWebInputException.class, 
			() -> controller.queryForPresidentBiography(0)
		);
			
		assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
	}
	
	@Test
	public void testQueryForPresidentBiographyDaoException() throws Exception {
		when(service.queryForPresidentBiography(anyInt()))
			.thenThrow(new RuntimeException("mock exception"));
		
		ServerErrorException ex = assertThrows(ServerErrorException.class, 
			() -> controller.queryForPresidentBiography(1)
		);
			
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
	}
}
