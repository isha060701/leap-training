package com.roifmr.presidents.restcontroller;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerErrorException;
import org.springframework.web.server.ServerWebInputException;

import com.roifmr.presidents.integration.PresidentsDao;
import com.roifmr.presidents.models.President;
import com.roifmr.presidents.service.PresidentsService;

/**
 * PresidentsController is a RESTful endpoint that handles requests for 
 * data about US presidents.
 */
@RestController
@RequestMapping("/presidents")
public class PresidentsController {
	@Autowired
	private Logger logger;

	@Autowired
	private PresidentsService service;
	
	
	@GetMapping
	public ResponseEntity<List<President>> queryForAllPresidents() {
		try {
			List<President> presidents = service.queryForAllPresidents();
			
			ResponseEntity<List<President>> responseEntity;
			
			if (presidents != null && presidents.size() > 0) {
				responseEntity = ResponseEntity.ok(presidents);
			}
			else {
				
				responseEntity = ResponseEntity.noContent().build();
			}
			return responseEntity;
		} 
		catch (Exception e) {
			
			logger.error("Exception while getting all presidents: " +  e);
			throw new ServerErrorException("Backend issue", e);
		}
	}

	
	@GetMapping("/{id}")
	public ResponseEntity<BiographyDto> queryForPresidentBiography(@PathVariable int id) {
		
		if (id <= 0) {
			throw new ServerWebInputException("id must be greater than 0");
		}
		try {
			String bioString = service.queryForPresidentBiography(id);
			
			ResponseEntity<BiographyDto> responseEntity;
			if (bioString != null) {
				BiographyDto biographyDto = new BiographyDto(bioString);
				responseEntity = ResponseEntity.ok(biographyDto); 
			}
			else {
				
				responseEntity = ResponseEntity.noContent().build();
			}
			return responseEntity;
		} 
		catch (Exception e) {
			logger.error("Exception while getting bio for president " + id + ": " + e);
			throw new ServerErrorException("Backend issue", e);
		}
	
	}
}
