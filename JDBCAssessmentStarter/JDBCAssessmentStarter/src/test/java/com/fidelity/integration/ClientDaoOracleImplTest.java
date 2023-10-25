package com.fidelity.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.fidelity.model.Client;
import com.fidelity.model.ClientRisk;

class ClientDaoOracleImplTest {
	ClientDao dao;
	JdbcTemplate jdbcTemplate;
	DbTestUtils dbTestUtils;
	SimpleDataSource dataSource;
	Connection connection;
	TransactionManager transactionManager;

	Client firstClient;
	Client lastClient;
	Client noPhoneNumClient;

	@BeforeEach
	void setUp() throws Exception {
		dataSource = new SimpleDataSource();
		connection = dataSource.getConnection();
		dbTestUtils = new DbTestUtils(connection);		
		jdbcTemplate = dbTestUtils.initJdbcTemplate();
		transactionManager = new TransactionManager(dataSource);

		// start the transaction
		transactionManager.startTransaction();
		
		dao = new ClientDaoOracleImpl(dataSource);

		firstClient = new Client(1, "Ford Prefect", ClientRisk.HIGH, "+441174960234"); 
		lastClient = new Client(6, "Slartibartfast", ClientRisk.LOW, "+353209174242"); 
		noPhoneNumClient = new Client(5, "Hotblack Desiato", ClientRisk.HIGH, null);
	}

	@AfterEach
	void tearDown() throws Exception {
		transactionManager.rollbackTransaction();
		dataSource.shutdown();
	}
	@Test
	void testGetClients_sizeIsGreaterThanOne() {
		List<Client> clients = dao.getClients();

		assertTrue(clients.size()>1);
	}
	
	@Test
	void testGetClients_retrieveFirstClientRow() {
		List<Client> clients = dao.getClients();

		assertEquals(firstClient, clients.get(0)); 
		
	}
	
	@Test
	void testGetClients_retrieveLastClientRow() {
		List<Client> clients = dao.getClients();

		assertEquals(lastClient, clients.get(clients.size() - 1)); 
	}

	@Test
	void testGetClients_retrieveClientWithoutPhoneNumber() {
		List<Client> clients = dao.getClients();

		assertTrue(clients.contains(noPhoneNumClient)); 
	}
	
	@Test
	void insertNullClient_ThrowsException(){
		assertThrows(NullPointerException.class,()->{
			dao.insertClient(null);
		});
	}
	@Test
	void testInsertClientWithPhoneNumber_checkIfNewClientGotAdded() {
		int clientId = 7;
		String clientQuery = "select client_name from aa_client where client_id = " + clientId;
		Client newClient = new Client(clientId, "Isha", ClientRisk.MEDIUM, "043");
		assertEquals(0, jdbcTemplate.queryForList(clientQuery).size());

		dao.insertClient(newClient);
		
		assertEquals(1, jdbcTemplate.queryForList(clientQuery).size());
	}
	@Test
	void testInsertClientWithPhoneNumber_clientTableSizeIncreasedByOne() {
		int clientId = 7;
		int oldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "aa_client");
		Client newClient = new Client(clientId, "Isha", ClientRisk.MEDIUM, "043");

		dao.insertClient(newClient);
		
		int newSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "aa_client");
		assertEquals(oldSize + 1, newSize);
		
	}
	@Test
	void testInsertClientWithPhoneNumber_clientPhoneTableSizeIncreasedByOne() {
		int clientId = 7;
		int oldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "aa_client_phone");
		Client newClient = new Client(clientId, "Isha", ClientRisk.MEDIUM, "043");

		dao.insertClient(newClient);
		
		int newSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "aa_client_phone");
		assertEquals(oldSize + 1, newSize);
		
	}
	@Test
	void testInsertClientWithoutPhoneNumber_checkIfNewClientGotAdded() {
		int clientId = 7;
		String clientQuery = "select client_name from aa_client where client_id = " + clientId;
		Client newClient = new Client(clientId, "Isha", ClientRisk.MEDIUM, null);
		assertEquals(0, jdbcTemplate.queryForList(clientQuery).size());

		dao.insertClient(newClient);
		
		assertEquals(1, jdbcTemplate.queryForList(clientQuery).size());
	}
	
	@Test
	void testInsertClientWithoutPhoneNumber_clientTableSizeIncreasedByOne() {
		int clientId = 7;
		int oldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "aa_client");
		Client newClient = new Client(clientId, "Isha", ClientRisk.MEDIUM, null);

		dao.insertClient(newClient);
		
		int newSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "aa_client");
		assertEquals(oldSize + 1, newSize);
		
	}
	
	@Test
	void testInsert_duplicateValuesThrowException() {
		Client newClient = new Client(3, "Isha", ClientRisk.MEDIUM, "043");
		int oldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "aa_client");
		
		assertThrows(DatabaseException.class, () -> {
			dao.insertClient(newClient);
		});
		

		int newSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "aa_client");
		assertEquals(oldSize, newSize);
	}
	
	@Test
	void cannotDeleteIdWhichDoesNotExist_throwsException() throws SQLException {

		int id = 10;
		assertThrows(DatabaseException.class, () -> {
			dao.deleteClient(id);
		});
	}
	
	@Test
	void testDeleteClient_WithoutPhoneNumber() throws SQLException {
		int oldSizeClients = JdbcTestUtils.countRowsInTable(jdbcTemplate, "aa_client");

		
		dao.deleteClient(noPhoneNumClient.getClientId());
		
		int newSizeClients = JdbcTestUtils.countRowsInTable(jdbcTemplate, "aa_client");
		
		assertEquals(oldSizeClients - 1, newSizeClients);
	}
	
	@Test
	void testDeleteClient_WithPhoneNumber_ClientPhoneTableRowDeleted() throws SQLException {
		int oldSizePhones = JdbcTestUtils.countRowsInTable(jdbcTemplate, "aa_client_phone");

		dao.deleteClient(firstClient.getClientId());
		
		int newSizePhones = JdbcTestUtils.countRowsInTable(jdbcTemplate, "aa_client_phone");
		
		assertEquals(oldSizePhones - 1, newSizePhones);
	}
	
	@Test
	void testDeleteClientWithPhoneNumber_ClientTableRowDeleted() throws SQLException {
		int oldSizeClients = JdbcTestUtils.countRowsInTable(jdbcTemplate, "aa_client");

		dao.deleteClient(firstClient.getClientId());
		
		int newSizeClients = JdbcTestUtils.countRowsInTable(jdbcTemplate, "aa_client");
		
		assertEquals(oldSizeClients - 1, newSizeClients);
	}
	
	@Test
	void CannotDeleteNegativeClientId_ThrowsException() {
		assertThrows(IllegalArgumentException.class,()->{
			dao.deleteClient(-1);
		});
	}
	
	

}
