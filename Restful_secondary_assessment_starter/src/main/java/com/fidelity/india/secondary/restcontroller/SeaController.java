package com.fidelity.india.secondary.restcontroller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fidelity.india.secondary.models.Bill;
import com.fidelity.india.secondary.models.Sea;
import com.fidelity.india.secondary.services.SeaDatabaseException;
import com.fidelity.india.secondary.services.SeaServiceImpl;


@RestController 
@RequestMapping("/sea")
public class SeaController {
	
	public static final String SEA_URL_BASE = "http://localhost:8888/marina/";
	
	@Autowired
	private Logger logger;  
	
	@Autowired
	private SeaServiceImpl service;
	
	@Autowired
	private RestTemplate restTemplate;  
	
	@GetMapping("/marina/{vessel_name}")  
	public ResponseEntity<Bill> getBill(@PathVariable String vessel_name) {
		
		System.out.println(vessel_name);
		
		if (vessel_name==null) {
			logger.error("vessel_name cant be null");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		String errorReason = "problem getting marina name for " + vessel_name;

		try {  
			ResponseEntity<Bill> billResponse;	

			Sea sea = service.getPortName(vessel_name);
			if (sea != null) {  
				Bill detailedBill = 
						getBillFromBackend(sea.getPort_name(),vessel_name);

				billResponse = ResponseEntity.ok(detailedBill);
			}
			else {
				logger.warn(vessel_name + " not found");
				billResponse = ResponseEntity.notFound().build();
			}
			return billResponse;
		}
		catch (SeaDatabaseException e) {
			logger.error("cannot getBill({}): {}", vessel_name, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private Bill getBillFromBackend(String port_name, String vessel_name) {
		logger.debug("getBill({},{}): enter", port_name, vessel_name);

		String forecastUrl = SEA_URL_BASE + port_name + "/" + vessel_name;

		
		ResponseEntity<Bill> forecastResponse = 
				restTemplate.getForEntity(forecastUrl, Bill.class);

		if (forecastResponse.getStatusCode() != HttpStatus.OK) {
			String message = "Error getting forecast for " + port_name + "," + vessel_name;
			throw new SeaDatabaseException(message);
		}

		Bill forecast = forecastResponse.getBody();
        System.out.println("From backend "+forecastResponse.getBody());
		return forecast;
	}

}
