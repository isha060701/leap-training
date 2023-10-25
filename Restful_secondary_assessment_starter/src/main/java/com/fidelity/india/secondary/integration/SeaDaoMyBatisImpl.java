package com.fidelity.india.secondary.integration;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fidelity.india.secondary.integration.mapper.SeaMapper;
import com.fidelity.india.secondary.models.Sea;

@Repository
public class SeaDaoMyBatisImpl implements SeaDao {
	
	@Autowired
	private Logger logger;
	
	@Autowired
	private SeaMapper mapper;

	@Override
	public Sea getPortName(String vessel) {
		// TODO Auto-generated method stub
		if(vessel==null)
		{
			throw new IllegalArgumentException("vessel name may not be null");
		}
		System.out.println("From Dao " +mapper.getPortName(vessel));
		return mapper.getPortName(vessel);
	}

}
