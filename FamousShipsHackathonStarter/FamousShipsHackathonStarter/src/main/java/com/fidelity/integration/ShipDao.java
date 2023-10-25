package com.fidelity.integration;

import java.util.List;

import com.fidelity.business.Ship;

public interface ShipDao {

	List<Ship> queryAllShips();
	Ship queryShipById(int id);
	String queryCaptainByShipName(String name);

}