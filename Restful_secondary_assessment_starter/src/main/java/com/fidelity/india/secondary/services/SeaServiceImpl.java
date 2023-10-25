package com.fidelity.india.secondary.services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fidelity.india.secondary.integration.SeaDaoMyBatisImpl;
import com.fidelity.india.secondary.models.Sea;

@Service
@Transactional
public class SeaServiceImpl implements SeaService {
	
	@Autowired
	private Logger logger;
	
	@Autowired
	private SeaDaoMyBatisImpl dao;

	@Override
	public Sea getPortName(String vessel) {
		// TODO Auto-generated method stub
		if(vessel==null)
		{
			throw new IllegalArgumentException("vessel cannot be empty");
		}
		Sea sea;
		try {
			System.out.println("From Serivce " +dao.getPortName(vessel).getPort_name());
			sea=dao.getPortName(vessel);
		}catch (DataAccessException e) {
			String msg = "Error getting vessel from the database.";
			logger.error(msg,e);
			throw new SeaDatabaseException(msg, e);
		}
		return sea;
	}

}
