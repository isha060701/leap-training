package com.fidelity.integration.mapper;

import java.util.List;

import com.fidelity.business.Ship;

public interface ShipMapper {
	List<Ship> getAllShips();
	Ship getShipById(int id);
	String getCaptainByShipName(String name);

}
