package com.fidelity.restservices;

import java.util.List;

import com.fidelity.business.Ship;

public interface ShipService {
	
	List<Ship> queryAllShips();
	Ship queryShipById(int id);
	String queryCaptainByShipName(String name);
}
