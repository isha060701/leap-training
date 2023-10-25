package com.roifmr.presidents.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.roifmr.presidents.integration.PresidentsDao;
import com.roifmr.presidents.models.President;

@Service
@Transactional
public class PresidentsServiceImpl implements PresidentsService {
	
	@Autowired
	private PresidentsDao dao;

	@Override
	public List<President> queryForAllPresidents() {
		// TODO Auto-generated method stub
		List<President> presidents;

		try {
			presidents = dao.queryForAllPresidents();
		} 
		catch (Exception e) {
			String msg = "Error querying all Presidents in the database.";
			throw new PresidentsDatabaseException(msg, e);
		}

		return presidents;
	}

	@Override
	public String queryForPresidentBiography(int id) {
		// TODO Auto-generated method stub
		validateId(id);

		String bio;
		try {
			bio=dao.queryForPresidentBiography(id);
		} 
		catch (Exception e) {
			String msg = String.format("Error querying For President with id = %d in the database.", id);
			throw new PresidentsDatabaseException(msg, e);
		}
		
		return bio;
	}
	
	private void validateId(int id) {
		if (id < 1) {
			throw new IllegalArgumentException("invalid President id " + id);
		}
	}

}
