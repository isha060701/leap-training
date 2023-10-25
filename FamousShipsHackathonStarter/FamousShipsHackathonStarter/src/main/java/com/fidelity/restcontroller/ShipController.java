package com.fidelity.restcontroller;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerErrorException;
import org.springframework.web.server.ServerWebInputException;

import com.fidelity.business.Ship;
import com.fidelity.restservices.ShipDatabaseException;
import com.fidelity.restservices.ShipService;

@RestController
@RequestMapping("/ships")
public class ShipController {
	
	@Autowired
	private Logger logger;
	
	@Autowired
	private ShipService service;
	
	@GetMapping
	public ResponseEntity<List<Ship>> queryForAllShips() {
		try {
			List<Ship> Ships = service.queryAllShips();
			
			ResponseEntity<List<Ship>> responseEntity;
			
			if (Ships != null && Ships.size() > 0) {
				responseEntity = ResponseEntity.ok(Ships);
			}
			else {
				
				responseEntity = ResponseEntity.noContent().build();
			}
			return responseEntity;
		} 
		catch (ShipDatabaseException e) {
			
			logger.error("Exception while getting all Ships: " +  e);
//			throw new ServerErrorException("Backend issue", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		}
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<Ship> queryForShipById(@PathVariable int id) {
		ResponseEntity<Ship> responseEntity;
		if (id <= 0) {
			//throw new ServerWebInputException("id must be greater than 0");
			logger.error("Invalid Id");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		try {
			Ship ship = service.queryShipById(id);
			
			
			if (ship != null) {
				responseEntity = ResponseEntity.ok(ship); 
			}
			else {
				
				responseEntity = ResponseEntity.noContent().build();
			}
			return responseEntity;
		} 
		catch (Exception e) {
			logger.error("Exception while getting ship with id= " + id + ": " + e);
			//throw new ServerErrorException("Backend issue", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	
	}
	
	@GetMapping("name/{name}")
	public ResponseEntity<ShipCaptainResponse> queryForPresidentBiography(@PathVariable String name) {
		
		if (name==null) {
			throw new ServerWebInputException("id must be greater than 0");
		}
		try {
			String captainString = service.queryCaptainByShipName(name);
			
			ResponseEntity<ShipCaptainResponse> responseEntity;
			if (captainString != null) {
				ShipCaptainResponse captainDto = new ShipCaptainResponse(captainString);
				responseEntity = ResponseEntity.ok(captainDto); 
			}
			else {
				
				responseEntity = ResponseEntity.noContent().build();
			}
			return responseEntity;
		} 
		catch (ServerErrorException e) {
			logger.error("Exception while getting bio for president " + name + ": " + e);
//			throw new ServerErrorException("Backend issue", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		}
	}

}
