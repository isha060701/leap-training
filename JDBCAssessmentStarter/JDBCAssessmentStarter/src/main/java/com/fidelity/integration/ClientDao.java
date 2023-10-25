package com.fidelity.integration;

import java.util.List;

import com.fidelity.model.Client;

public interface ClientDao {
	// you may not change any of these methods
	List<Client> getClients();
	void insertClient(Client client);
	void deleteClient(int clientId);
}
