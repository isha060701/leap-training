package com.fidelity.integration;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class TransactionManager {
	
	private DataSource dataSource;
	private Connection connection;

	public TransactionManager(DataSource dataSource) {

		this.dataSource = dataSource;
	}
	
	
	public void startTransaction() {
		try {
			connection=dataSource.getConnection();
			connection.setAutoCommit(false);
			
		}catch(SQLException e)
		{
			throw new DatabaseException("unable to begin a transaction"+e);
		}
	}
	
	public void rollbackTransaction() {
		
		try {
			
			connection.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DatabaseException("unable to roll back a transaction"+e);
		}
		
	}
	
	public void commitTransaction() {
		
		try {
			
			connection.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DatabaseException("unable to commit a transaction"+e);
		}
		
	}
	
	
}
