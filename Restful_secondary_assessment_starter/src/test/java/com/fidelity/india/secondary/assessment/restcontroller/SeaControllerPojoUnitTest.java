package com.fidelity.india.secondary.assessment.restcontroller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fidelity.india.secondary.models.Bill;
import com.fidelity.india.secondary.models.Sea;
import com.fidelity.india.secondary.restcontroller.SeaController;
import com.fidelity.india.secondary.services.SeaServiceImpl;



class SeaControllerPojoUnitTest {

	@InjectMocks
	private SeaController controller;
	@Mock
	private Logger mockLogger;
	@Mock
	private RestTemplate mockRestTemplate;
	@Mock
	private SeaServiceImpl mockService;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGetPortName_CheckSuccessCodeValue() {
		String vessel = "Bystolic";
		String port = "Belmullet";
		

		when(mockService.getPortName(vessel))
			.thenReturn(new Sea(port));

		Bill forecast = new Bill();
		forecast.setVesselName("Bystolic");
		when(mockRestTemplate.getForEntity(SeaController.SEA_URL_BASE + port+ "/" + vessel, Bill.class))
			.thenReturn(ResponseEntity.ok(forecast));

		ResponseEntity<Bill> response = controller.getBill(vessel);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void testGetPortName_JsonValueReturnedIsCorect() {
		String vessel = "Bystolic";
		String port = "Belmullet";

		when(mockService.getPortName(vessel))
			.thenReturn(new Sea(port));

		Bill forecast = new Bill();
		forecast.setVesselName("Bystolic");
		forecast.setCountry("Ireland");
		when(mockRestTemplate.getForEntity(SeaController.SEA_URL_BASE + port+ "/" + vessel, Bill.class))
			.thenReturn(ResponseEntity.ok(forecast));

		ResponseEntity<Bill> response = controller.getBill(vessel);
		
		
		assertEquals("Ireland", response.getBody().getCountry());
	}

	@Test
	public void testGetPortName_VesselNameNotFound() {
		String vessel = "Bystolic";
		String port = "Belmullet";

		when(mockService.getPortName(vessel))
			.thenReturn(null);
		
		ResponseEntity<Bill> response = controller.getBill(vessel);
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
}
