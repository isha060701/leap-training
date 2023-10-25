package com.roifmr.presidents.restcontroller;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.roifmr.presidents.models.President;
import com.roifmr.presidents.service.PresidentsService;
import com.roifmr.presidents.integration.PresidentsDao;


@WebMvcTest(PresidentsController.class)
public class PresidentsControllerWebLayerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private PresidentsService mockService;
	
	private static final List<President> presidents = Arrays.asList(
		new President(1, "George", "Washington", 1789, 1797, "georgewashington.jpg", "Chopped down a cherry tree"), 
		new President(2, "John", "Adams", 1797, 1801, "johnadams.jpg", "Learned and thoughtful")
	);
	
	@Test
	public void testQueryForAllPresidents() throws Exception {
		when(mockService.queryForAllPresidents())
			.thenReturn(presidents);
		
		mockMvc.perform(get("/presidents"))
		       //.andDo(print())
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.length()").value(2))
		       .andExpect(jsonPath("$[0].lastName").value("Washington"))
		       .andExpect(jsonPath("$[1].lastName").value("Adams"));
	}
	
	@Test
	public void testQueryForAllPresidentsEmptyList() throws Exception {
		when(mockService.queryForAllPresidents())
			.thenReturn(new ArrayList<>());
		
		mockMvc.perform(get("/presidents"))
			    //.andDo(print())
		       .andExpect(status().isNoContent())
		       .andExpect(content().string(is(emptyOrNullString())));
	}
	
	@Test
	public void testQueryForAllPresidentsNullList() throws Exception {
		when(mockService.queryForAllPresidents())
			.thenReturn(null);
		
		mockMvc.perform(get("/presidents"))
		       //.andDo(print())
		       .andExpect(status().isNoContent())
		       .andExpect(content().string(is(emptyOrNullString())));
	}
	
	@Test
	public void testQueryForAllPresidentsDaoException() throws Exception {
		when(mockService.queryForAllPresidents())
			.thenThrow(new RuntimeException("mock exception"));
		
		mockMvc.perform(get("/presidents"))
		       //.andDo(print())
		       .andExpect(status().isInternalServerError())
		       .andExpect(content().string(is(emptyOrNullString())));
	}

	@Test
	public void testQueryForPresidentBiography() throws Exception {
		int id = 2;
		String bioString = presidents.get(1).getBiography();

		when(mockService.queryForPresidentBiography(id))
			.thenReturn(bioString);
		
		mockMvc.perform(get("/presidents/" + id))
		       //.andDo(print())
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.bio").value(bioString));
	}
	
	@Test
	public void testQueryForPresidentBiographyInvalidId() throws Exception {
		int id = -1;
		
		mockMvc.perform(get("/presidents/" + id))
		       //.andDo(print())
		       .andExpect(status().isBadRequest())
		       .andExpect(content().string(is(emptyOrNullString())));
	}
	
	@Test
	public void testQueryForPresidentBiographyNonExistantId() throws Exception {
		int id = 99;
		when(mockService.queryForPresidentBiography(id))
			.thenReturn(null);
		
		mockMvc.perform(get("/presidents/" + id))
		       //.andDo(print())
		       .andExpect(status().isNoContent())
		       .andExpect(content().string(is(emptyOrNullString())));
	}
	
	@Test
	public void testQueryForPresidentBiographyDaoException() throws Exception {
		int id = 5;
		when(mockService.queryForPresidentBiography(id))
			.thenThrow(new RuntimeException("mock exception"));
		
		mockMvc.perform(get("/presidents/" + id))
		       //.andDo(print())
		       .andExpect(status().isInternalServerError())
		       .andExpect(content().string(is(emptyOrNullString())));
	}
	
	
}
