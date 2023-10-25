package com.roifmr.presidents.integration;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.roifmr.presidents.integration.mapper.PresidentsMapper;
import com.roifmr.presidents.models.President;

@Repository
public class PresidentsMyBatisDaoImpl implements PresidentsDao {
	@Autowired
	private Logger logger;

	@Autowired
	private PresidentsMapper mapper;
	
	@Override
	public List<President> queryForAllPresidents() {
		logger.debug("queryForAllPresidents()");

		return mapper.getAllPresidents();
	}

	@Override
	public String queryForPresidentBiography(int id) {
		logger.debug("queryForPresidentsBiography({})", id);

		if (id <= 0) {
			throw new IllegalArgumentException(
				"id " + id + " is invalid: must be greater than zero");
		}
		return mapper.getBiographyById(id);
	}

}
