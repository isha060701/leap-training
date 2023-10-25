package com.fidelity.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;
import static org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables;

import com.fidelity.business.Widget;

/**
 * This is an integration test for WarehouseDaoMyBatisImpl.
 * 
 * Notice that the database schema and data scripts are run after setting the DataSource. 
 * This insures the database is in a known state prior to running the tests.
 * The database scripts are in the folder: src/test/resources/
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
// HINT: See slide 3-21

@SpringBootTest
@Transactional
public class WarehouseDaoMyBatisImplIntegrationTest {

	// TODO: note the definitions of the fields for the DAO and JdbcTemplate
	//       (no code changes required)
	@Autowired
	private WarehouseDao dao;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	// Because the test database is tiny, we can check all products.
	// If the database was larger, we could just spot-check a few widgets.
	// These are the values defined in the data-dev.sql script.
	
	private static List<Widget> allWidgets = Arrays.asList(
		new Widget(1, "Low Impact Widget", 12.99, 2, 3),
		new Widget(2, "Medium Impact Widget", 42.99, 5, 5),
		new Widget(3, "High Impact Widget", 89.99, 10, 8)
	);

	@Test
	void testGetAllWidgets_Success() {
		List<Widget> widgets = dao.getAllWidgets();
		
		// verify the Widgets from the database 
		assertEquals(allWidgets, widgets);
	}

	// TODO: write a test case that verifies the DAO's getAllWidgets method
	//       behaves as expected when the Widgets table is empty.
	@Test
	void testGetAllWidgets_WidgetsTableIsEmpty() {
		// Note how we delete all rows in the Widgets table using
		// JdbcTestUtils.deleteFromTables()
		deleteFromTables(jdbcTemplate, "widgets");

		// TODO: what should the DAO's getAllWdigets() return if the Widgets table 
		//       is empty? Write the test code that verifies the correct behavior
		assertTrue(dao.getAllWidgets().size()==0);
	}

	@Test
	void testGetWidget_Present() {
		Widget widget = dao.getWidget(1);
		
		// verify the Widget from the database
		assertEquals(allWidgets.get(0), widget);
	}

	// TODO: write a test case that verifies the DAO's getWidget method
	//       behaves as expected when it's passed the id of widget that's not present
	@Test
	void testGetWidget_NotPresent() {
        Widget widget = dao.getWidget(4);
		
		// verify the Widget from the database
		assertEquals(null, widget);
	}

	@Test
	void testDeleteWidget_Sucess() {
		int rowsBeforeDelete = countRowsInTable(jdbcTemplate, "widgets");
		int id = 1;
		// verify that Widget 1 is in the database
		assertEquals(countRowsInTableWhere(jdbcTemplate, "widgets", "id = " + id), 1);

		int rows = dao.deleteWidget(id);
		
		// verify that 1 row was deleted
		assertEquals(1, rows);
		int rowsAfterDelete = countRowsInTable(jdbcTemplate, "widgets");
		assertEquals(rowsBeforeDelete - 1, rowsAfterDelete);
		// verify that Widget 1 is no longer in the database
		assertEquals(countRowsInTableWhere(jdbcTemplate, "widgets", "id = " + id), 0);
	}

	// TODO: write a test case that verifies the DAO's deleteWidget method
	//       does not delete a row if the given widget id is not present
	@Test
	void testDeleteWidget_NotPresent() {
		int row=dao.deleteGadget(4);
		assertEquals(0,row);
	}

	@Test
	void testInsertWidget_Success() {
		int rowsBeforeInsert = countRowsInTable(jdbcTemplate, "widgets");
		int id = 42;
		
		// verify that Widget with id = 42 is NOT in the database
		assertEquals(countRowsInTableWhere(jdbcTemplate, "widgets", "id = " + id), 0);

		Widget widget = new Widget(id, "Test widget", 4.52, 20, 10);

		int rows = dao.insertWidget(widget);
		
		// verify that 1 row was inserted
		assertEquals(1, rows);
		int rowsAfterInsert = countRowsInTable(jdbcTemplate, "widgets");
		assertEquals(rowsBeforeInsert + 1, rowsAfterInsert);
		
		// verify that all properties of the new widget were added to the database
		assertEquals(1, countRowsInTableWhere(jdbcTemplate, "widgets", """
					id = 42
				and description = 'Test widget'
				and price = 4.52
				and gears = 20
				and sprockets = 10
			"""));
	}

	// TODO: write a negative test case that verifies the database is
	//       not changed if you attempt to insert a widget with duplicate id.
	// HINT: the attempt to add the duplicate id will cause Spring to throw
	//       a DuplicateKeyException.
	@Test
	void testInsertWidget_DuplicateId() {
		assertThrows(DuplicateKeyException.class,()->{
			dao.insertWidget(allWidgets.get(0));
		});
	}

	@Test
	void testUpdateWidget() {
		int id = 1;
		
		// load the original Widget from the database
		Widget originalWidget = loadWidgetFromDb(id);
		
		// modify the local Widget
		originalWidget.setPrice(originalWidget.getPrice() + 1.0);

		int rows = dao.updateWidget(originalWidget);
		
		// verify that 1 row was updated
		assertEquals(1, rows);
		
		// reload widget from database 
		Widget updatedWidget = loadWidgetFromDb(id);
		
		// verify that only the price was updated in the database
		assertEquals(updatedWidget, originalWidget);
	}

	@Test
	void testUpdateWidget_NotPresent() {
		int id = 999;
		Widget widget = new Widget(id, "Test widget", 4.52, 20, 10);

		int rows = dao.updateWidget(widget);
		
		assertEquals(0, rows);
	}

	// ***** Utility Methods Used in the Tests *****
	
	// Load the Widget with the specified id from the database
	private Widget loadWidgetFromDb(int id) {
		String sql = "select * from widgets where id = " + id;
		
		return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> 
			new Widget(rs.getInt("id"), rs.getString("description"), rs.getDouble("price"), 
					   rs.getInt("gears"), rs.getInt("sprockets")));
	}
	
}
