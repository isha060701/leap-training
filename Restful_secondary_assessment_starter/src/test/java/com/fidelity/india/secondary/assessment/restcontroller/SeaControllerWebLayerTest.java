package com.fidelity.india.secondary.assessment.restcontroller;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.fidelity.india.secondary.integration.SeaDaoMyBatisImpl;
import com.fidelity.india.secondary.integration.mapper.SeaMapper;
import com.fidelity.india.secondary.models.Bill;
import com.fidelity.india.secondary.models.Sea;
import com.fidelity.india.secondary.restcontroller.SeaController;
import com.fidelity.india.secondary.services.SeaDatabaseException;
import com.fidelity.india.secondary.services.SeaServiceImpl;


@WebMvcTest(SeaController.class)
class SeaControllerWebLayerTest {
	
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private SeaServiceImpl mockService;
	@MockBean
	private RestTemplate mockRestTemplate;
	
	@MockBean
	private SeaDaoMyBatisImpl mockDao;
	
	@MockBean
	private SeaMapper mapper;

	@Test
	public void testGetPortName_Success() throws Exception {
		Sea sea = new Sea("Belmullet");
		
		when(mockService.getPortName("Bystolic"))
			.thenReturn(sea);

		Bill bill = 
				new Bill("Belmullet","Ireland","Bystolic",453.51,75,"€34013.25");
		ResponseEntity<Bill> response = ResponseEntity.ok(bill);
		String forecastUrl = SeaController.SEA_URL_BASE + sea.getPort_name()+ "/" +"Bystolic" ;
		when(mockRestTemplate.getForEntity(forecastUrl, Bill.class))
			.thenReturn(response);

		mockMvc.perform(get("/sea/marina/Bystolic"))
		       .andDo(print())
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.country").value("Ireland"));
	}
	
	
	
	@Test
	public void testGetPortName_NullJsonReturned() throws Exception {
		when(mockService.getPortName("Bystolic"))
			.thenReturn(null);
		
		mockMvc.perform(get("/sea/marina/Bystolic"))
		       //.andDo(print())
		       .andExpect(status().is4xxClientError())
		       .andExpect(content().string(is(emptyOrNullString())));
	}
	
	@Test
	public void testGetPortName_throwsException() throws Exception {
		when(mockService.getPortName("Bystolic"))
			.thenThrow(new SeaDatabaseException());
		
		mockMvc.perform(get("/sea/marina/Bystolic"))
		       .andExpect(status().isInternalServerError())
		       .andExpect(content().string(is(emptyOrNullString())));
	}

}
