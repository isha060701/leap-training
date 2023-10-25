package com.fidelity.integration;

import java.util.List;

import com.fidelity.model.Client;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fidelity.model.ClientRisk;
public class ClientDaoOracleImpl implements ClientDao {
	
	private DataSource datasource;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public ClientDaoOracleImpl(DataSource dataSource) {
        this.datasource = dataSource;
    }
	

    
    
	@Override
	public List<Client> getClients() {
		// TODO Auto-generated method stub
		String sql = """			
				SELECT c.client_id, 
					   c.client_name, 
					   c.client_risk, 
					   cp.phone_number
				FROM      aa_client       c 
				LEFT JOIN aa_client_phone cp 
				ON        c.client_id   = cp.client_id
				ORDER BY  c.client_id				
			""";
			
			List<Client> clients = new ArrayList<>();
			try (Connection connection = datasource.getConnection();
			         PreparedStatement stmt = connection.prepareStatement(sql)){
				
				ResultSet rs = stmt.executeQuery();
				clients = handleResults(rs);		
			} 
			catch (SQLException e) {
				e.printStackTrace();
		        logger.error("Cannot execute getClients", e);
				throw new DatabaseException("Cannot execute getClients", e);
			}
			return clients;
	}
	
	private List<Client> handleResults(ResultSet rs) throws SQLException {
		List<Client> clients = new ArrayList<>();
		while (rs.next()) {
			int clientId = rs.getInt("client_id");
			String clientName = rs.getString("client_name");
			ClientRisk clientRisk = ClientRisk.of(rs.getString("client_risk"));
			String workPhone = rs.getString("phone_number");
			Client client = new Client(clientId, clientName, clientRisk, workPhone);
			clients.add(client);
		}
		return clients;
	}

	@Override
	public void insertClient(Client client) {
		// TODO Auto-generated method stub
		Connection conn = null;
		try (Connection connection = datasource.getConnection()){
			conn = datasource.getConnection();
			insertClientOnly(conn, client);
			if(client.getWorkPhone()!=null)
			{
				insertClientPhone(conn, client);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
	        logger.error("Cannot execute insertClient", e);
			throw new DatabaseException("Cannot execute insertClient", e);
		}

	}
	
	private void insertClientOnly(Connection conn, Client client) {
		Objects.requireNonNull(client);
		
		String sql = """
			INSERT INTO aa_client 
			       (client_name, client_risk, client_id) 
			VALUES (?, ?, ?)				
		""";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			stmt.setString(1, client.getClientName());
			stmt.setString(2, client.getClientRisk().getCode());
			stmt.setInt(3, client.getClientId());
			stmt.executeUpdate();
		} 
		catch (SQLException e) {
			e.printStackTrace();
	        logger.error("Cannot insert Client", e);
			throw new DatabaseException("Cannot insert Client", e);
		}
	}

	private void insertClientPhone(Connection conn, Client client) {
		String sql = """
			INSERT INTO aa_client_phone 
			       (client_id, phone_number) 
			VALUES (?, ?)				
		""";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, client.getClientId());
			stmt.setString(2, client.getWorkPhone());
			stmt.executeUpdate();
		} 
		catch (SQLException e) {
			e.printStackTrace();
	        logger.error("Cannot insert Client Phone", e);
			throw new DatabaseException("Cannot insert Client Phone", e);
		}		
		
	}
	@Override
	public void deleteClient(int clientId) {
		if(clientId<=0)
		{
			throw new IllegalArgumentException("Client ID cannot be negative");
		}
		try(Connection conn = datasource.getConnection()) {
			if(phoneNumberExists(clientId))
			{
				deleteClientPhone(conn, clientId);
			}
			deleteClientOnly(conn, clientId);
		} 
		catch (SQLException e) {
			e.printStackTrace();
			logger.error("Cannot execute deleteClient for " + clientId, e);
			throw new DatabaseException("Cannot delete client", e);
		} 
	}

	private void deleteClientOnly(Connection conn, int clientId) {
		String sql = """
			DELETE FROM aa_client 
			 WHERE client_id = ?				
		""";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, clientId);
			int rowsDeleted=stmt.executeUpdate();
			if(rowsDeleted==0)
			{
				throw new DatabaseException("Database does not have the id"+clientId);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
			logger.error("Cannot execute deleteClientOnly for " + clientId, e);
			throw new DatabaseException("Cannot delete client", e);
		}
	}

	private void deleteClientPhone(Connection conn, int clientId) {
		String sql = """
			DELETE FROM aa_client_phone 
			 WHERE client_id = ?				
		""";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, clientId);
			stmt.executeUpdate();
		} 
		catch (SQLException e) {
			e.printStackTrace();
			logger.error("Cannot execute deleteClientPhone for " + clientId, e);
			throw new DatabaseException("Cannot delete client", e);
		}
	}

	private boolean phoneNumberExists(int clientId) {
	    String query = "SELECT COUNT(client_id) FROM aa_client_phone WHERE client_id = ?";
	    try (Connection connection = datasource.getConnection();
	         PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setInt(1, clientId);
	        try (ResultSet resultSet = statement.executeQuery()) {
	            if (resultSet.next()) {
	                int rowCount = resultSet.getInt(1);
	                return rowCount == 1; 
	            }
	        }
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	logger.error("Error checking if Phone Number for that client exists", e);
	        throw new DatabaseException("Error checking if Phone Number for that client exists", e);
	    }
	    return false; 
	}
}
