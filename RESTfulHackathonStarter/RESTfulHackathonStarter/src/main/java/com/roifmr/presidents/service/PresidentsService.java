package com.roifmr.presidents.service;

import java.util.List;

import com.roifmr.presidents.models.President;

public interface PresidentsService {
	List<President> queryForAllPresidents();

	String queryForPresidentBiography(int id);

}
