package com.fidelity.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * DaoTest is an integration test for ShipDaoMyBatisImpl.
 * 
 * To verify that the DAO is actually working, we'll need to query the database 
 * directly, so we'll use Spring's JdbcTestUtils class, which has methods like 
 * countRowsInTable() and deleteFromTables().
 *
 * @author ROI Instructor
 *
 */
@SpringBootTest
@Transactional
class ShipDaoTest {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ShipDaoImpl dao;
	
	@Test
	void testQueryAllShips_Success()
	{
		assertTrue(dao.queryAllShips().size()>2);
	}
	
	@Test
	void testQueryAllShips_TableIsEmpty() {
		deleteFromTables(jdbcTemplate, "famousships");
		assertTrue(dao.queryAllShips().size()==0);

	}
	
	@Test
	void testQueryForShipById_IdIsPresent() {
		assertNotNull(dao.queryShipById(1));
		
	}
	
	@Test
	void testQueryForShipById_InvalidId() {
		assertThrows(IllegalArgumentException.class,()->{
			dao.queryShipById(-1);
		});
		
	}
	
	@Test
	void testQueryForShipByName_NameIsPresent() {
		assertNotNull(dao.queryCaptainByShipName("RMS Titanic"));
		
	}
	
	@Test
	void testQueryForShipById_InvalidName() {
		assertThrows(IllegalArgumentException.class,()->{
			dao.queryCaptainByShipName(null);
		});
		
	}

}
