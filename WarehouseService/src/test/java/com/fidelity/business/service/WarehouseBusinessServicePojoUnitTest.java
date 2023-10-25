package com.fidelity.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;

import com.fidelity.business.Widget;
import com.fidelity.integration.WarehouseDao;

/**
 * JUnit tests for WarehouseBusinessService using Mockito for managing mock objects.
 * 
 * Note that this is a pure unit test, not an integration test: Spring is not 
 * involved in this test at all. We are testing the business service class as a POJO
 * in complete isolation, so we don't want Spring configuration issues to 
 * cause these tests to fail. The other test classes in this package will do 
 * integration testing to verify the Spring configuration. 
 * 
 * The business service has a dependency  on the data access object.
 * Mockito is used to mock the data access object.
 * That way we can focus entirely on the business service functionality.
 * 
 * Spring Boot includes Mockito, but if you are running unit tests
 * without Spring Boot, you need to add Mockito dependencies to pom.xml:
 * 		<dependency>
 *			<groupId>org.mockito</groupId>
 *			<artifactId>mockito-core</artifactId>
 *			<version>3.3.3</version>
 *			<scope>test</scope>
 *		</dependency>
 *		<dependency>
 *			<groupId>org.mockito</groupId>
 *			<artifactId>mockito-junit-jupiter</artifactId>
 *			<version>3.3.3</version>
 *			<scope>test</scope>
 *		</dependency>
 *
 * You also may need to change the version of the junit-platform-surefire-provider dependency:
 *		<dependency>
 *			<groupId>org.junit.platform</groupId>
 *			<artifactId>junit-platform-surefire-provider</artifactId>
 *			<version>1.3.2</version>
 *		</dependency>
 * 
 * 
 * @author ROI Instructor
 *
 */

// TODO: note that there are no Spring annotations on this POJO unit test.
//       Mockito handles all the mocking; Spring is not used at all. 
//       (no code changes required)
class WarehouseBusinessServicePojoUnitTest {
	// TODO: note the definition of the mock WarehouseDao
	//       (no code changes required)
	@Mock
	private WarehouseDao mockDao;
	
	// TODO: note the definition of the business service instance that is being tested
	//       (no code changes required)
	@InjectMocks
	private WarehouseBusinessServiceImpl service;
	
	// TODO: note the call to openMocks() in the @BeforeEach method.
	//       This calls the WarehouseBusinessServiceImpl constructor and
	//       injects a mock DAO into the business service object.
	//       (no code changes required)
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);		
	}

	// TODO: write a test case that verifies the business service's findAllWidgets method
	//       behaves as expected when the DAO returns a list with several widgets
	// HINT: see slide 3-18
	@Test
	void testFindAllWidgets() {
		List<Widget> expectedWidgets = List.of(
				new Widget(1, "Low Impact Widget", 12.99, 2, 3),
				new Widget(2, "High Impact Widget", 15.99, 4, 5));
		when(mockDao.getAllWidgets())
				.thenReturn(expectedWidgets);
		List<Widget> actualWidgets = service.findAllWidgets();
		assertEquals(expectedWidgets, actualWidgets);
	}

 

	// TODO: write a test case that verifies the business service's findAllWidgets 
	//       method behaves as expected when the DAO returns an empty list
	@Test
	void testFindAllWidgets_DaoReturnsEmptyList() {
		when(mockDao.getAllWidgets())
			.thenReturn(Collections.emptyList());
		assertTrue(service.findAllWidgets().isEmpty());
	}

	// TODO: note the following test case, which verifies the business service's findAllWidgets
	//       method behaves as expected when the DAO throws a RuntimeException
	//       (no code changes required)
	@Test
	void testFindAllWidgets_DaoThrowsException() {
		// Configure the mock DAO to throw an exception when getAllWidgets is called
		when(mockDao.getAllWidgets())
        	.thenThrow(new RuntimeException("mock DAO exception"));
	
        assertThrows(WarehouseDatabaseException.class, () -> {
			service.findAllWidgets();
		});
	}

	// the remaining test cases test different scenarios for the business service's methods

	@Test
	void testFindWidgetById() {
		int id = 1;
		Widget firstWidget = new Widget(id, "Mock Widget 1", 12.99, 2, 3);
		
		when(mockDao.getWidget(id))
			.thenReturn(firstWidget);
		
		Widget widget = service.findWidgetById(id);
		
		// verify the Widget 
		assertEquals(firstWidget, widget);
	}

	@Test
	void testAddWidget() {
		Widget widget = new Widget(1, "Mock Widget 1", 4.52, 20, 10);
		
		when(mockDao.insertWidget(widget))
			.thenReturn(1);

		int rowsInserted = service.addWidget(widget);
		
		assertEquals(1, rowsInserted);		
	}

	@Test
	void testAddWidget_DaoThrowsWarehouseDatabaseException() {
		int id = 1;
		Widget widget = new Widget(id, "Mock Widget 1", 12.99, 2, 3);
		
		when(mockDao.insertWidget(widget))
        	.thenThrow(new WarehouseDatabaseException("mock DAO exception"));
	
        assertThrows(WarehouseDatabaseException.class, () -> {
			service.addWidget(widget);
		});
	}

	@Test
	void testAddWidget_DaoThrowsDuplicateKeyException() {
		int id = 1;
		Widget widget = new Widget(id, "Mock Widget 1", 12.99, 2, 3);
		
		when(mockDao.insertWidget(widget))
        	.thenThrow(new DuplicateKeyException("mock DAO exception"));
	
        assertThrows(DuplicateKeyException.class, () -> {
			service.addWidget(widget);
		});
	}
	
	@Test
	void testAddWidget_NullWidget() {
        assertThrows(IllegalArgumentException.class, () -> {
			service.addWidget(null);
		});
	}
	
	@Test
	void testAddWidget_WidgetMissingDescription() {
		Widget widget = new Widget(999, "", 12.99, 2, 3);

        assertThrows(IllegalArgumentException.class, () -> {
			service.addWidget(widget);
		});
	}
	
	@Test
	void testUpdateWidget() {
		Widget originalWidget = new Widget(1, "Mock Widget 1", 4.52, 20, 10);
		
		when(mockDao.updateWidget(originalWidget))
			.thenReturn(1);
		
		int count = service.modifyWidget(originalWidget);
		
		assertEquals(1, count);						
	}
	
	@Test
	void testDeleteWidget() {
		int id = 1;
		when(mockDao.deleteWidget(id))
			.thenReturn(1);
		
		int count = service.removeWidget(id);
		
		assertEquals(1, count);
	}
}
