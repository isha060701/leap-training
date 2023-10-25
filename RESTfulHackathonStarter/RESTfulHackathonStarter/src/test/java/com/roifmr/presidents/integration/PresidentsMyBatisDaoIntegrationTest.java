package com.roifmr.presidents.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables;

import org.hsqldb.lib.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.roifmr.presidents.models.President;
import com.roifmr.presidents.restcontroller.BiographyDto;

@SpringBootTest
@Transactional
public class PresidentsMyBatisDaoIntegrationTest {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private PresidentsDao dao;
	
	@Test
	void testQueryForAllPresidents_Success() {
		java.util.List<President> presidents=dao.queryForAllPresidents();
		assertEquals(presidents.get(0).getFirstName(), "George");
	}
	
	@Test
	void testQueryForAllPresidents_PresidentsTableIsEmpty() {
		deleteFromTables(jdbcTemplate, "presidents");
		assertTrue(dao.queryForAllPresidents().size()==0);
	}
	
	@Test
	void testQueryForPresidentById_IdIsPresent() {
		assertNotNull(dao.queryForPresidentBiography(1));
		
	}
	
	@Test
	void testQueryForPresidentById_IdIsNotPresent() {
		assertNull(dao.queryForPresidentBiography(12));
		
	}

}
