package com.fidelity.restservices;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fidelity.business.Ship;

import com.fidelity.integration.ShipDaoImpl;

@Service
@Transactional
public class ShipServiceImpl implements ShipService {
	
	@Autowired
	private Logger logger;
	
	@Autowired
	private ShipDaoImpl dao;

	@Override
	public List<Ship> queryAllShips() {
		// TODO Auto-generated method stub
		List<Ship> ships;

		try {
			ships = dao.queryAllShips();
		} 
		catch (DataAccessException e) {
			String msg = "Error querying all Presidents in the database.";
			logger.error(msg,e);
			throw new ShipDatabaseException(msg, e);
		}

		return ships;
	}

	@Override
	public Ship queryShipById(int id) {
		// TODO Auto-generated method stub
		validateId(id);

		Ship ship = null;
		try {
			ship = dao.queryShipById(id);
		} 
		catch (Exception e) {
			String msg = String.format("Error querying For Ship with id = %d in the Warehouse database.", id);
			throw new ShipDatabaseException(msg, e);
		}
		
		return ship;
	}

	@Override
	public String queryCaptainByShipName(String name) {
		// TODO Auto-generated method stub
		validateName(name);

		String captain = null;
		try {
			captain = dao.queryCaptainByShipName(name);
		} 
		catch (Exception e) {
			String msg = String.format("Error querying For Ship with name = %s in the Warehouse database.", name);
			throw new ShipDatabaseException(msg, e);
		}
		
		return captain;
	}
	
	private void validateId(int id) {
		if (id < 1) {
			throw new IllegalArgumentException("invalid Ship id " + id);
		}
	}
	
	private void validateName(String name) {
		if (name==null || name=="") {
			throw new IllegalArgumentException("Name cannot be null or empty");
		}
	}

}
