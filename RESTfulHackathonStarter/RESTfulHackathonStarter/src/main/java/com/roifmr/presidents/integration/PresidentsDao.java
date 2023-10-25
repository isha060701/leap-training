package com.roifmr.presidents.integration;

import java.util.List;

import com.roifmr.presidents.models.President;

public interface PresidentsDao {

	List<President> queryForAllPresidents();

	String queryForPresidentBiography(int id);

}