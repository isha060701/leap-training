package com.fidelity.india.secondary.assessment.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.fidelity.india.secondary.integration.SeaDao;
import com.fidelity.india.secondary.models.Sea;


@SpringBootTest
@Transactional
class DaoIntegrationTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private SeaDao dao;
	
	
	@Test
	public void testGetPortName_Success() {
		Sea expectedSea = new Sea("Belmullet");
		
		Sea actualSea = dao.getPortName("Bystolic");
		
		assertEquals(expectedSea, actualSea);
	}
	
	@Test
	public void testGetPortName_NotFound() {
		
		Sea actualSea = dao.getPortName("Bystoli");
		
		assertNull(actualSea);
	}
	
	@Test
	public void testGetPortName_VesselNameNullThrowsException() {
		
		
		
		assertThrows(IllegalArgumentException.class, ()->{
			Sea actualSea = dao.getPortName(null);
		});
	}
	
	

}
