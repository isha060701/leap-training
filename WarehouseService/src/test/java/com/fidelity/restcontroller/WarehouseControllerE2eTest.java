package com.fidelity.restcontroller;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;
import static org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables;

import com.fidelity.business.Gadget;
import com.fidelity.business.Widget;

/**
 * These test cases assert the behavior of our deployed service. To do that we will
 * start the application up and listen for a connection like it would do in
 * production, and then send an HTTP request and assert the response.
 * 
 * Note that Spring Boot has provided a TestRestTemplate for you automatically.
 * All you have to do is @Autowired it.
 * 
 * Instead of using a specific port number when constructing the server URL, simply use a 
 * relative URL. For example:
 * 	   String request = "/warehouse/widgets";
 * 
 * By using webEnvironment = WebEnvironment.RANDOM_PORT in SpringBootTest, a random port 
 * will be selected and added to the URL automatically.
 * The port that is being used appears in the Console window when the application is run.
 *
 * Also note the use of @Sql on the class to execute the database setup scripts before
 * each test case. Because @SpringBootTest runs Tomcat in a different thread than the
 * test cases themselves, @Transactional has no effect here. So we need to re-initialize 
 * the database before each test case.
 * Just another reason not to use the production database in testing :)
 * 
 * The database scripts referenced in @Sql are in the folder src/test/resources
 * 
 * For some test cases we'll need to query or modify the database directly, so we'll
 * use Spring's JdbcTestUtils class, which has methods like countRowsInTable() and
 * deleteFromTables().
 *  
 * Note that Spring Boot needs to find an application class in order to scan
 * for components. The trivial class com.fidelity.TestApplication in src/test/java 
 * contains the @SpringBootApplication annotation, which triggers the component scan.
 * 
 * @author ROI Instructor
 */

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@Sql({"classpath:schema-dev.sql", "classpath:data-dev.sql"}) 
public class WarehouseControllerE2eTest {
	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private JdbcTemplate jdbcTemplate;  // for executing SQL queries
	
	// **** Widget Tests ****
	/**
	 * This test verifies the WarehouseController can query successfully for all the
	 * Widgets in the Warehouse.
	 */
	@Test
	public void testQueryForAllWidgets() {
		// get the row count from the Widgets table
		int widgetCount = countRowsInTable(jdbcTemplate, "widgets");
		
		ResponseEntity<Widget[]> response = 
				restTemplate.getForEntity("/warehouse/widgets", Widget[].class);
		
		// verify the response HTTP status is OK
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		// verify that the service returned all Widgets in the database
		Widget[] responseWidgets = response.getBody();
		assertEquals(widgetCount, responseWidgets.length); 
		
		// spot-check a few Widgets
		assertEquals(new Widget(1, "Low Impact Widget", 12.99, 2, 3), responseWidgets[0]);
		assertEquals(new Widget(3, "High Impact Widget", 89.99, 10, 8), responseWidgets[2]);
	}

	/**
	 * This test verifies the WarehouseController successfully handles the case
	 * where there are no Widgets in the Warehouse.
	 */
	@Test
	public void testQueryForAllWidgets_NoWidgetsInDb() {
		// delete all rows from the Widgets table
		deleteFromTables(jdbcTemplate, "widgets");
		
		String request = "/warehouse/widgets";

		ResponseEntity<String> response = 
				restTemplate.getForEntity(request, String.class);
		
		// verify the response HTTP status is 204 (NO_CONTENT)
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		
		// verify that the response body is empty
		assertTrue(response.getBody() == null || response.getBody().isBlank());
	}

	/**
	 * This test verifies the WarehouseController can query successfully for one Widget.
	 */
	@Test
	public void testQueryForWidgetById() {
		String request = "/warehouse/widgets/3";

		ResponseEntity<Widget> response = 
				restTemplate.getForEntity(request, Widget.class);
		
		// verify the response HTTP status
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		// verify that the service returned the correct Widget
		assertEquals(new Widget(3, "High Impact Widget", 89.99, 10, 8), response.getBody());
	}

	/**
	 * This test verifies the WarehouseController handles a query for a non-existent Widget.
	 */
	@Test
	public void testQueryForWidgetById_NotPresent() {
		String request = "/warehouse/widgets/99";

		ResponseEntity<Widget> response = 
				restTemplate.getForEntity(request, Widget.class);
		
		// verify the response HTTP status
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	/**
	 * This test verifies the WarehouseController can successfully add a Widget to the
	 * Warehouse.
	 */
	@Test
	public void testAddWidgetToWarehouse() throws Exception {
		// ARRANGE
		int widgetCount = countRowsInTable(jdbcTemplate, "widgets");
		int id = 42;
		Widget newWidget = new Widget(id, "Test widget", 4.52, 20, 10);
		// ACT
		ResponseEntity<DatabaseRequestResult> response = 
			restTemplate.postForEntity("/warehouse/widgets", newWidget, 
									   DatabaseRequestResult.class);
		// ASSERT
		// verify the response HTTP status and response body
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1, response.getBody().getRowCount()); // {"rowCount": 1}

		// verify that one row was added to the Widgets table
		int newWidgetCount = countRowsInTable(jdbcTemplate, "widgets");
		assertEquals(widgetCount + 1, newWidgetCount);

		// verify that the new widget is in the Widgets table
		assertEquals(1, countRowsInTableWhere(jdbcTemplate, "widgets", """
					id = 42
				and description = 'Test widget'
				and price = 4.52
				and gears =  20
				and sprockets = 10
			"""));
	}

	/**
	 * This test verifies the WarehouseController can successfully remove a Widget from
	 * the Warehouse.
	 */
	@Test
	public void testRemoveWidgetFromWarehouse() throws Exception{
		int widgetCount = countRowsInTable(jdbcTemplate, "widgets");

		int id = 1;
		String request = "/warehouse/widgets/" + id;
		
		RequestEntity<Void> requestEntity = 
				RequestEntity.delete(new URI(request)).build();
		
		ResponseEntity<DatabaseRequestResult> response = 
				restTemplate.exchange(requestEntity, DatabaseRequestResult.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1, response.getBody().getRowCount()); // {"rowCount": 1}

		// verify that one row was deleted from the Widgets table
		int newWidgetCount = countRowsInTable(jdbcTemplate, "widgets");
		assertEquals(widgetCount - 1, newWidgetCount);
		
		// verify that the widget is no longer in the Widgets table
		assertEquals(countRowsInTableWhere(jdbcTemplate, "widgets", "id = " + id), 0);
	}
	
	/**
	 * This test verifies the WarehouseController will return the HTTP status
	 * NotFound when attempting to remove a Widget that is not in the Warehouse.
	 */
	@Test
	public void testRemoveWidgetFromWarehouse_NotPresent() throws Exception{
		int widgetCount = countRowsInTable(jdbcTemplate, "widgets");

		int id = 99;
		String request = "/warehouse/widgets/" + id;
		
		RequestEntity<Void> requestEntity = 
				RequestEntity.delete(new URI(request)).build();
		
		ResponseEntity<DatabaseRequestResult> response = 
				restTemplate.exchange(requestEntity, DatabaseRequestResult.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(0, response.getBody().getRowCount()); // {"rowCount": 0}

		// verify that no rows were deleted from the Widgets table
		assertEquals(countRowsInTable(jdbcTemplate, "widgets"), widgetCount);
	}

	// **** Gadget Tests ****
	
	/**
	 * This test verifies the WarehouseController can query successfully for all the
	 * Gadgets in the Warehouse.
	 */
	@Test
	public void testQueryForAllGadgets() {
		// get the row count from the Gadgets table
		int gadgetCount = countRowsInTable(jdbcTemplate, "gadgets");
		
		String request = "/warehouse/gadgets";

		ResponseEntity<Gadget[]> response = 
				restTemplate.getForEntity(request, Gadget[].class);
		
		// verify the response HTTP status
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		// verify that the service returned all Gadgets in the database
		Gadget[] responseGadgets = response.getBody();
		assertEquals(gadgetCount, responseGadgets.length); 
		
		// spot-check a few Gadgets
		assertEquals(new Gadget(1, "Two Cylinder Gadget", 19.99, 2), responseGadgets[0]);
		assertEquals(new Gadget(3, "Eight Cylinder Gadget", 49.99, 8), responseGadgets[2]);
	}

	/**
	 * This test verifies the WarehouseController successfully handles the case
	 * where there are no Gadgets in the Warehouse.
	 */
	@Test
	public void testQueryForAllGadgets_NoGadgetsInDb() {
		// delete all rows from the Gadgets table
		deleteFromTables(jdbcTemplate, "gadgets");
		
		String request = "/warehouse/gadgets";

		ResponseEntity<String> response = 
				restTemplate.getForEntity(request, String.class);
		
		// verify the response HTTP status is 204
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		
		// verify that the response body is empty
		assertTrue(response.getBody() == null || response.getBody().isBlank());
	}

	/**
	 * This test verifies the WarehouseController can query successfully for one Gadget.
	 */
	@Test
	public void testQueryForGadgetById() {
		String request = "/warehouse/gadgets/3";

		ResponseEntity<Gadget> response = 
				restTemplate.getForEntity(request, Gadget.class);
		
		// verify the response HTTP status
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		// verify that the service returned the correct Gadget
		assertEquals(new Gadget(3, "Eight Cylinder Gadget", 49.99, 8), response.getBody());
	}

	/**
	 * This test verifies the WarehouseController handles a query for a non-existent Gadget.
	 */
	@Test
	public void testQueryForGadgetById_NotPresent() {
		String request = "/warehouse/gadgets/99";

		ResponseEntity<Gadget> response = 
				restTemplate.getForEntity(request, Gadget.class);
		
		// verify the response HTTP status
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	/**
	 * This test verifies the WarehouseController can successfully add a Gadget to the
	 * Warehouse.
	 */
	@Test
	public void testAddGadgetToWarehouse() throws Exception {
		String tableName = "gadgets";
		int gadgetCount = countRowsInTable(jdbcTemplate, tableName);
		
		int id = 42;
		Gadget newGadget = new Gadget(id, "Test Gadget", 19.99, 2);

		String request = "/warehouse/gadgets";
		
		ResponseEntity<DatabaseRequestResult> response = 
				restTemplate.postForEntity(request, newGadget, 
						                   DatabaseRequestResult.class);
		
		// verify the response HTTP status and response body
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(1, response.getBody().getRowCount()); // {"rowCount": 1}
		
		// verify that one row was added to the Gadgets table
		int newWidgetCount = countRowsInTable(jdbcTemplate, tableName);
		assertEquals(gadgetCount + 1, newWidgetCount);
		
		// verify that the new Gadget is in the Gadgets table
		assertEquals(1, countRowsInTableWhere(jdbcTemplate, tableName, """
					id = 42
				and description = 'Test Gadget'
				and price = 19.99
				and cylinders = 2
			"""));
	}

	/**
	 * This test verifies the WarehouseController can successfully remove a Gadget from
	 * the Warehouse.
	 */
	@Test
	public void testRemoveGadgetFromWarehouse() throws Exception{
		int gadgetCount = countRowsInTable(jdbcTemplate, "gadgets");

		int id = 1;
		String request = "/warehouse/gadgets/" + id;
		
		RequestEntity<Void> requestEntity = 
				RequestEntity.delete(new URI(request)).build();
		
		ResponseEntity<DatabaseRequestResult> response = 
				restTemplate.exchange(requestEntity, DatabaseRequestResult.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1, response.getBody().getRowCount()); // {"rowCount": 1}

		// verify that one row was deleted from the Gadgets table
		int newWidgetCount = countRowsInTable(jdbcTemplate, "gadgets");
		assertEquals(gadgetCount - 1, newWidgetCount);
		
		// verify that the gadget is no longer in the Gadgets table
		assertEquals(countRowsInTableWhere(jdbcTemplate, "gadgets", "id = " + id), 0);
	}

	/**
	 * This test verifies the WarehouseController will return the HTTP status
	 * NotFound when attempting to remove a Gadget that is not in the Warehouse.
	 */
	@Test
	public void testRemoveGadgetFromWarehouse_NotPresent() throws Exception{
		int gadgetCount = countRowsInTable(jdbcTemplate, "gadgets");

		int id = 99;
		String request = "/warehouse/gadgets/" + id;
		
		RequestEntity<Void> requestEntity = 
				RequestEntity.delete(new URI(request)).build();
		
		ResponseEntity<DatabaseRequestResult> response = 
				restTemplate.exchange(requestEntity, DatabaseRequestResult.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(0, response.getBody().getRowCount()); // {"rowCount": 0}

		// verify that no rows were deleted from the Gadgets table
		assertEquals(countRowsInTable(jdbcTemplate, "gadgets"), gadgetCount);
	}

}

