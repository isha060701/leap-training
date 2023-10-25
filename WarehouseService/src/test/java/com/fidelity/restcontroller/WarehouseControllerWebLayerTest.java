package com.fidelity.restcontroller;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLEngineResult.Status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fidelity.business.Widget;
import com.fidelity.business.service.WarehouseBusinessService;

/**
 * The WarehouseController has a dependency on the WarehouseBusinessService. 
 * The WarehouseBusinessService will be Autowired by Spring into the WarehouseController.
 * 
 * We will test the WarehouseController using @WebMvcTest.
 * We use @MockBean to create and inject a mock for the WarehouseBusinessService.
 * If you don’t do this the application context cannot start.
 * We set the expectations using MockMvc.
 *
 * Note that Spring Boot needs to find an application class in order to scan
 * for components. The trivial class com.fidelity.TestApplication in src/test/java 
 * contains the @SpringBootApplication annotation, which triggers the component scan.
 * 
 * @author ROI Instructor
 */

// TODO: add the required annotation to make this class a Spring web layer test
// HINT: see slide 3-25
@WebMvcTest
public class WarehouseControllerWebLayerTest {
	// TODO: note the definition of the fields for the MockMvc and business service
	//       (no code changes required)
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WarehouseBusinessService mockBusinessService;
	
	private List<Widget> widgets;
	
	@BeforeEach
	public void init() {
		widgets = Arrays.asList(
			new Widget(1, "Test Widget 1", 1.99, 2, 10),
			new Widget(2, "Test Widget 2", 2.99, 4, 20),
			new Widget(3, "Test Widget 3", 3.99, 6, 30)
		);
	}

	// TODO: review the first four test cases and make sure you understand them.
	
	/**
	 * This test verifies the WarehouseController can query successfully for the
	 * Widget with the specified id.
	 */
	@Test
	public void testQueryForWidgetById() throws Exception {
		int id = 1;
		Widget firstWidget = new Widget(id, "Low Impact Widget", 12.99, 2, 3);
		
		when(mockBusinessService.findWidgetById(id))
			.thenReturn(firstWidget);
		
		// In this test case, we'll be very thorough and test every property 
		// of the returned JSON

		mockMvc.perform(get("/warehouse/widgets/1"))
			   .andDo(print())
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.id").value(id))
			   .andExpect(jsonPath("$.description").value("Low Impact Widget"))
			   .andExpect(jsonPath("$.price").value("12.99"))
			   .andExpect(jsonPath("$.gears").value("2"))
			   .andExpect(jsonPath("$.sprockets").value("3"));
	}

	/**
	 * This test verifies the WarehouseController can query successfully for all the
	 * Widgets.
	 */
	@Test
	public void testQueryForAllWidgets() throws Exception {
		when(mockBusinessService.findAllWidgets())
			.thenReturn(widgets);
		
		// The previous test case proves that the controller returns all the properties 
		// of a single widget. So in this test case, we won't be as exhaustive in our 
		// testing: we'll just test one property of each returned JSON object.

		mockMvc.perform(get("/warehouse/widgets"))
			   //.andDo(print())
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.length()").value(3))
			   .andExpect(jsonPath("$[0].description").value("Test Widget 1"))
			   .andExpect(jsonPath("$[2].description").value("Test Widget 3"));
	}
	
	/**
	 * This test verifies that the WarehouseController returns an HTTP No_Content
	 * status when the widget list is empty.
	 */
	@Test
	public void testQueryForAllWidgets_DaoReturnsEmptyList() throws Exception {
		when(mockBusinessService.findAllWidgets())
			.thenReturn(new ArrayList<Widget>());
		
		mockMvc.perform(get("/warehouse/widgets"))
			   //.andDo(print())
			   .andExpect(status().isNoContent())
			   .andExpect(content().string(is(emptyOrNullString())));
	}

	// TODO: add a test case that verifies the WarehouseController returns a
	//       server error HTTP status when the business service throws an exception.
	// HINT: see slide 3-26
	@Test
	public void testQueryForAllWidgets_DaoThrowsException() throws Exception {
		when(mockBusinessService.findAllWidgets())
			.thenThrow(new RuntimeException());
		
		mockMvc.perform(get("/warehouse/widgets"))
			   //.andDo(print())
			   .andExpect(status().is5xxServerError())
			   .andExpect(content().string(is(emptyOrNullString())));
	}

	// TODO: add a test case to verify the WarehouseController can 
	//		 successfully add a Widget to the Warehouse.
	// HINT: see slide 3-24
	@Test
	public void testAddWidgetToWarehouse() throws Exception {
		Widget widget = new Widget(42, "Test widget", 4.52, 20, 10);
		
		when(mockBusinessService.addWidget(widget))
			.thenReturn(1);
		
		// Creating the ObjectMapper object
		ObjectMapper mapper = new ObjectMapper();
		// Converting the Object to JSONString
		String jsonString = mapper.writeValueAsString(widget);
		
		// expected JSON response from a successful POST request:
		//      {"rowCount": 1}

		mockMvc.perform(post("/warehouse/widgets")
						   .contentType(MediaType.APPLICATION_JSON)
						   .content(jsonString))
			   //.andDo(print())
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.rowCount").value(1));
	}
	
	@Test
	public void testAddWidgetToWarehouse_cantInsert() throws Exception {
		Widget widget = new Widget(3, "Test widget", 4.52, 20, 10);
		
		when(mockBusinessService.addWidget(widget))
			.thenReturn(0);
		
		// Creating the ObjectMapper object
		ObjectMapper mapper = new ObjectMapper();
		// Converting the Object to JSONString
		String jsonString = mapper.writeValueAsString(widget);
		
		// expected JSON response from a successful POST request:
		//      {"rowCount": 1}

		mockMvc.perform(post("/warehouse/widgets")
						   .contentType(MediaType.APPLICATION_JSON)
						   .content(jsonString))
			   //.andDo(print())
			   .andExpect(status().is4xxClientError());
	}

	// TODO: add a test case to verify the WarehouseController can 
	//		 successfully remove a Widget from the Warehouse.
	// HINT: if you were testing this manually with Insomnia, how would know
	//       the operation succeeded?
	@Test
	public void testRemoveWidgetFromWarehouse() throws Exception{
		int id = 1;
		when(mockBusinessService.removeWidget(id))
			.thenReturn(1);
		
		mockMvc.perform(delete("/warehouse/widgets/" + id))
			   //.andDo(print())
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.rowCount").value(1));
	}

	// TODO: add a test case to verify the WarehouseController can 
	//		 successfully update an existing Widget in the Warehouse.
	// HINT: this will be similar to the test case for adding a widget.
	@Test
	public void testUpdateWidgetInWarehouse() throws Exception{
		Widget widget = new Widget(42, "Test widget", 4.52, 20, 10);
		
		when(mockBusinessService.modifyWidget(widget))
			.thenReturn(1);
		
		// Creating the ObjectMapper object
		ObjectMapper mapper = new ObjectMapper();
		
		// Converting the Object to JSONString
		String jsonString = mapper.writeValueAsString(widget);
		
		mockMvc.perform(put("/warehouse/widgets")
							.contentType(MediaType.APPLICATION_JSON)
							.content(jsonString))
			   //.andDo(print())
			   .andExpect(status().isAccepted())
			   .andExpect(jsonPath("$.rowCount").value(1));
	}

}
