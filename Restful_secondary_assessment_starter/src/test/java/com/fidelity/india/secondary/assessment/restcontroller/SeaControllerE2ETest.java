package com.fidelity.india.secondary.assessment.restcontroller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import com.fidelity.india.secondary.models.Bill;
import com.fidelity.india.secondary.restcontroller.SeaController;


@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
class SeaControllerE2ETest {

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	public void testGetPortName_CheckSuccessStatusCode() {
		String request = "/sea/marina/Bystolic";
		

		ResponseEntity<Bill> response = 
				restTemplate.getForEntity(request, Bill.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		
	}
	
	@Test
	public void testGetPortName_ReceivedCorrectJsonObject() {
		String request = "/sea/marina/Bystolic";
		Bill expectedForecast = 
			new Bill("Belmullet","Ireland","Bystolic",485.88,43,"€20892.84");

		ResponseEntity<Bill> response = 
				restTemplate.getForEntity(request, Bill.class);
		
		
		Bill forecast = response.getBody();
		assertEquals("Belmullet", forecast.getMarina());
		assertEquals("Ireland", forecast.getCountry());
	}
	
	@Test
	public void testGetPortName_UnknownCityNotFound () {
		String request = "/sea/marina/Bystol";

		ResponseEntity<Bill> response = 
				restTemplate.getForEntity(request, Bill.class);
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}
}
