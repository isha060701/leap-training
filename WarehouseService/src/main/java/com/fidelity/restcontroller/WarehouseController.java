package com.fidelity.restcontroller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerErrorException;
import org.springframework.web.server.ServerWebInputException;

import com.fidelity.business.Gadget;
import com.fidelity.business.Product;
import com.fidelity.business.Widget;
import com.fidelity.business.service.WarehouseBusinessService;

/**
 * WarehouseController is a RESTful web service.
 * It provides web methods to manage Widgets and Gadgets 
 * in the Warehouse database.
 * 
 * There is no business logic in this class; all business rules in
 * terms of validating data, defining transaction boundaries, etc.,
 * are implemented in the business service.
 * 
 * @author ROI Instructor
 *
 */

// TODO: add the required Spring annotations:
//       1. identify this class as a RESTful controller
//       2. configure the URL that will trigger method calls
// HINT: see slide 1-22
@RestController
@RequestMapping("/warehouse")
public class WarehouseController {
	
	// TODO: note the autowired business service field. The RESTful controller
	//       will delegate all input validation and database operations to the 
	//       business service.
	//       (no code changes required)
	@Autowired
	private WarehouseBusinessService service;
	
	public WarehouseController() {}

	@GetMapping(value="/ping",
				produces=MediaType.ALL_VALUE)
	public String ping() {
		return "Warehouse web service is alive at " + LocalDateTime.now();
	}
	
	// **** Widget methods ****
	
	// TODO: define CRUD operations for widgets
	
	private static final String DB_ERROR_MSG = 
			"Error communicating with the warehouse database";
	
	// Get all widgets
	@GetMapping("/widgets")
	public ResponseEntity<List<Widget>> queryForAllWidgets() {
		ResponseEntity<List<Widget>> result;
		List<Widget> products;
		try {
			products = service.findAllWidgets();
		} 
		catch (Exception e) {
			throw new ServerErrorException(DB_ERROR_MSG, e);
		}
		
		if (products.size() > 0) {
			result = ResponseEntity.ok(products);
		}
		else {
			result = ResponseEntity.noContent().build();
		}
		return result;
	}

	@GetMapping("/widgets/{id}")
	public Widget queryForWidgetById(@PathVariable int id) {
		Widget widget = null;
		try {
			widget = service.findWidgetById(id);
		} 
		catch (Exception e) {
			throw new ServerErrorException(DB_ERROR_MSG, e);
		}
		
		if (widget == null) {
			throw new ServerWebInputException(
					"No widget in the warehouse with id = " + id);
		}
		return widget;
	}

	@DeleteMapping("/widgets/{id}")
	public DatabaseRequestResult removeWidget(@PathVariable("id") int id) {
		int rows = 0;
		try {
			rows = service.removeWidget(id);
		} 
		catch (Exception e) {
			throw new ServerErrorException(DB_ERROR_MSG, e);
		}
		if (rows == 0) {
			throw new ServerWebInputException(
					"No widget in the warehouse with id = " + id);
		}
		return new DatabaseRequestResult(rows);
	}

	@PostMapping("/widgets")
	@ResponseStatus(HttpStatus.CREATED)  // default HTTP response status 201
	public ResponseEntity<DatabaseRequestResult> insertWidget(@RequestBody Widget widget) {
		ResponseEntity<DatabaseRequestResult> result = null;
		int count = 0;
		try {
			count = service.addWidget(widget);
		} 
		catch (DuplicateKeyException e) {
			throw new ServerWebInputException("widgets id is a duplicate: " + widget);
		}
		catch (Exception e) {
			throw new ServerErrorException(DB_ERROR_MSG, e);
		}
		if (count == 0) {
			result=ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			//throw new ServerWebInputException("Can't insert widget " + widget);
		}
		else {
			result=ResponseEntity.ok(new DatabaseRequestResult(count));
		}
		return result;
	}

	@PutMapping("/widgets")
	@ResponseStatus(HttpStatus.ACCEPTED)  // default HTTP response status 202
	public DatabaseRequestResult updateWidget(@RequestBody Widget widget) {
		int count = 0;
		try {
			count = service.modifyWidget(widget);
		} 
		catch (Exception e) {
			throw new ServerErrorException(DB_ERROR_MSG, e);
		}
		if (count == 0) {
			throw new ServerWebInputException("Can't update widget " + widget);
		}
		return new DatabaseRequestResult(count);
	}

	// Gadget methods
	
	@GetMapping("/gadgets")
	public ResponseEntity<List<Gadget>> queryForAllGadgets() {
		ResponseEntity<List<Gadget>> result;
		List<Gadget> products;
		try {
			products = service.findAllGadgets();
		} 
		catch (Exception e) {
			throw new ServerErrorException(DB_ERROR_MSG, e);
		}
		if (products.size() > 0) {
			result = ResponseEntity.ok(products);
		}
		else {
			result = ResponseEntity.noContent().build();
		}
		return result;
	}

	@GetMapping("/gadgets/{id}")
	public Gadget queryForGadgetById(@PathVariable("id") int id) {
		Gadget gadget = null;
		try {
			gadget = service.findGadgetById(id);
		} 
		catch (Exception e) {
			throw new ServerErrorException(DB_ERROR_MSG, e);
		}
		if (gadget == null) {
			throw new ServerWebInputException(
					"No gadgets in the warehouse with id = " + id);
		}
		return gadget;
	}

	@DeleteMapping("/gadgets/{id}")
	public DatabaseRequestResult removeGadget(@PathVariable("id") int id) {
		int rows = 0;
		try {
			rows = service.removeGadget(id);
		} 
		catch (Exception e) {
			throw new ServerErrorException(DB_ERROR_MSG, e);
		}
		if (rows == 0) {
			throw new ServerWebInputException(
					"No gadgets in the warehouse with id = " + id);
		}
		return new DatabaseRequestResult(rows);
	}

	@PostMapping("/gadgets")
	@ResponseStatus(HttpStatus.CREATED)  // default HTTP response status 201
	public DatabaseRequestResult insertGadget(@RequestBody Gadget gadget) {
		int count = 0;
		try {
			count = service.addGadget(gadget);
		} 
		catch (Exception e) {
			throw new ServerErrorException(DB_ERROR_MSG, e);
		}
		if (count == 0) {
			throw new ServerWebInputException("Can't insert gadget " + gadget);
		}
		return new DatabaseRequestResult(count);
	}

	@PutMapping("/gadgets")
	@ResponseStatus(HttpStatus.ACCEPTED)  // default HTTP response status 202
	public DatabaseRequestResult updateGadget(@RequestBody Gadget gadget) {
		int count = 0;
		try {
			count = service.modifyGadget(gadget);
		} 
		catch (Exception e) {
			throw new ServerErrorException(DB_ERROR_MSG, e);
		}
		if (count == 0) {
			throw new ServerWebInputException("Can't update gadget " + gadget);
		}
		return new DatabaseRequestResult(count);
	}
	
	@GetMapping("/products")
	public ResponseEntity<List<Product>> queryForAllProducts() {
		ResponseEntity<List<Product>> result;
		List<Product> products = new ArrayList<>();
		try {
			products = service.findAllProducts();
		} 
		catch (Exception e) {
			throw new ServerErrorException(DB_ERROR_MSG, e);
		}
		if (products.size() > 0) {
			result = ResponseEntity.ok(products);
		}
		else {
			result = ResponseEntity.noContent().build();
		}
		return result;
	}
	
}

