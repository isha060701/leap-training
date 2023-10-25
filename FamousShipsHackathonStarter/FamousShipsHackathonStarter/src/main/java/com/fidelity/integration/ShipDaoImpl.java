package com.fidelity.integration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fidelity.business.Ship;
import com.fidelity.integration.mapper.ShipMapper;

@Repository
public class ShipDaoImpl implements ShipDao {
	
	@Autowired
	private ShipMapper mapper;

	@Override
	public List<Ship> queryAllShips() {
		// TODO Auto-generated method stub
		return mapper.getAllShips();
	}

	@Override
	public Ship queryShipById(int id) {
		// TODO Auto-generated method stub
		if (id <= 0) {
			throw new IllegalArgumentException(
				"id " + id + " is invalid: must be greater than zero");
		}
		return mapper.getShipById(id);
	}

	@Override
	public String queryCaptainByShipName(String name) {
		// TODO Auto-generated method stub
		if (name==null || name=="") {
			throw new IllegalArgumentException(
				" is invalid: name cannot be null or empty");
		}
		return mapper.getCaptainByShipName(name);
	}

}
