package com.fidelity.business.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;
import org.springframework.transaction.annotation.Transactional;

import com.fidelity.business.Gadget;
import com.fidelity.business.Widget;

/**
 * WarehouseBusinessServiceIntegrationTest is an integration test for WarehouseBusinessServiceImpl.
 * 
 * Notice that the database schema and data scripts are run
 * after setting the DataSource. 
 * The database scripts are in the folder: src/test/resources/
 * This guarantees that the database is in a known state before the tests are run.
 *  
 * To verify that the DAO is actually working, we'll need to query the database 
 * directly, so we'll use Spring's JdbcTestUtils class, which has methods like 
 * countRowsInTable() and deleteFromTables().
 *
 * Notice the use of @Transactional to automatically rollback 
 * any changes to the database that may be made in a test.
 *
 * Note that Spring Boot needs to find an application class in order to scan
 * for components. The trivial class com.fidelity.TestApplication in src/test/java 
 * contains the @SpringBootApplication annotation, which triggers the component scan.
 * 
 * @author ROI Instructor
 *
 */

// TODO: add the necessary Spring annotations to:
//       1. define this class as a Spring Boot test
//       2. enable automatic transaction management so database changes 
//          are rolled back after every test case
// HINT: See slide 3-20


@SpringBootTest
@Transactional
class WarehouseBusinessServiceIntegrationTest {
	// TODO: add an autowired field for the business service being tested
	@Autowired
	private WarehouseBusinessService service;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;  // for executing SQL queries
	
	// TODO: define a List of all widgets in the database
	private static List<Widget> allWidgets = List.of(
		new Widget(1, "Low Impact Widget", 12.99, 2, 3),
		new Widget(2, "Medium Impact Widget", 42.99, 5, 5),
		new Widget(3, "High Impact Widget", 89.99, 10, 8)
	);
	// Because the test database is tiny, we can check all products.
	// If the database was larger, we could just spot-check a few products.

	// TODO: write integration tests to verify the operation of the
	//       WarehouseBusinessService methods:
	//			findAllWidgets()
	//			findWidgetById()
	//			removeWidget()
	//			addWidget()
	//			modifyWidget()
	
	@Test
	void testGetAllWidgets() {
		List<Widget> widgets = service.findAllWidgets();
		
		// verify the collection of Widgets
		assertEquals(allWidgets, widgets);
	}

	@Test
	void testFindWidgetById() {
		int id = 1;
		Widget firstWidget = new Widget(id, "Low Impact Widget", 12.99, 2, 3);
		
		Widget widget = service.findWidgetById(id);
		
		// verify the Widget
		assertEquals(firstWidget, widget);
	}

	@Test
	void testDeleteWidget() {
		int id = 1;
		
		// verify that Widget 1 IS in the database
		assertEquals(countRowsInTableWhere(jdbcTemplate, "widgets", "id = " + id), 1);

		int rows = service.removeWidget(id);
		
		// verify that 1 row was deleted
		assertEquals(1, rows);
		
		// verify that Widget 1 is NOT in the database
		assertEquals(countRowsInTableWhere(jdbcTemplate, "widgets", "id = " + id), 0);
	}
	
	@Test
	void testInsertWidget() {
		int id = 42;
		
		// verify Widget 42 is NOT in the database
		assertEquals(countRowsInTableWhere(jdbcTemplate, "widgets", "id = " + id), 0);

		Widget widget = new Widget(id, "Test widget", 4.52, 20, 10);

		int rows = service.addWidget(widget);
		
		// verify that 1 row was inserted		
		assertEquals(1, rows);
		
		// verify that Widget 42 iIS in the database
		assertEquals(countRowsInTableWhere(jdbcTemplate, "widgets", "id = " + id), 1);
	}
	
	@Test
	void testUpdateWidget() {
		int id = 1;
		
		// load Widget 1 from the database
		Widget localWidget = loadWidgetFromDb(id);
		
		// modify the local Widget 1
		localWidget.setPrice(localWidget.getPrice() + 1.0);

		int rows = service.modifyWidget(localWidget);
		
		// verify that 1 row was updated
		assertEquals(1, rows);
		
		// reload widget from database 
		Widget updatedWidget = loadWidgetFromDb(id);
		
		// verify that only the price was updated
		assertEquals(updatedWidget, localWidget);

	}

	// ***** Utility Method to Load a Widget from the Database *****

	private Widget loadWidgetFromDb(int id) {
		String sql = "select * from widgets where id = " + id;
		
		return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> 
			new Widget(rs.getInt("id"), rs.getString("description"), rs.getDouble("price"), 
					   rs.getInt("gears"), rs.getInt("sprockets")));
	}

}