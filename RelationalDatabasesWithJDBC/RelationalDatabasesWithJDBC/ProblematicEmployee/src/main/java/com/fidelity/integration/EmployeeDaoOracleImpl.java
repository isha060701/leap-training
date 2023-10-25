package com.fidelity.integration;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fidelity.model.Employee;

public class EmployeeDaoOracleImpl extends EmployeeDao {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final String insertEmployee = "INSERT INTO emp (ename, job, mgr, hiredate, sal, comm, deptno, empno) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	private final String updateEmployee = "UPDATE emp SET ename = ?, job = ?, mgr = ?, hiredate = ?, "
			+ "sal = ?, comm = ?, deptno = ? WHERE empno = ?";

	public EmployeeDaoOracleImpl(DataSource ds) {
		super(ds);
	}

	@Override
	public List<Employee> queryAllEmployees() {
		String sql = "SELECT empno, ename, job, mgr, TO_CHAR(hiredate, 'DD-MON-YYYY') AS hiredate, salary, comm, deptno FROM emp";

		List<Employee> emps = new ArrayList<>();
		try (Connection connection = getDataSource().getConnection()) {
			try (PreparedStatement stmt = connection.prepareStatement(sql)) {
				emps = getAndHandleResults(stmt);
			}
		} 
		catch (SQLException e) {
			logger.error("Cannot execute queryAllEmployees: {}", sql, e);
			throw new DatabaseException("Cannot execute queryAllEmployees", e);
		}
		return emps;
	}

	@Override
	/*
	 * This method only returns one Employee, but the generic query handler
	 * (getAndHandleResults) returns a list.
	 * 
	 * Return the first item in the list, if there is one. Doing it this way avoids
	 * an index out of bounds exception.
	 */
	public Employee queryEmployeeById(int empNo) {
		String sql = "SELECT empno, ename, job, mgr, TO_CHAR(hiredate, 'DD-MON-YYYY') AS hiredate, sal, comm, deptno FROM emp "
				+ "WHERE empno = ?";

		List<Employee> emps = new ArrayList<>();
		try (Connection connection = getDataSource().getConnection()){
			try (PreparedStatement stmt = connection.prepareStatement(sql)) {
				stmt.setInt(1, empNo);
				emps = getAndHandleResults(stmt);
			}
		} 
		catch (SQLException e) {
			logger.error("Cannot execute queryEmployeeById: {} for {}", sql, empNo, e);
			throw new DatabaseException("Cannot execute queryEmployeeById", e);
		}
		Employee employee =  emps.size() > 0 ? emps.get(0) : null;
		logger.info(employee.toString());
		
		return employee;
	}

	/**
	 * Query Employees by name
	 */
	@Override
	public List<Employee> queryEmployeeByName(String name) {
		String sql = "SELECT empno, ename, job, mgr, TO_CHAR(hiredate, 'DD-MON-YYYY') AS hiredate, sal, comm, deptno FROM emp "
				+ "WHERE ename = ?";

		List<Employee> emps = new ArrayList<>();
		try (Connection connection = getDataSource().getConnection()){
			try (PreparedStatement stmt = connection.prepareStatement(sql)) {
				stmt.setString(1, name);
				emps = getAndHandleResults(stmt);
			}
		} 
		catch (SQLException e) {
			logger.error("Cannot execute queryEmployeeByName: {} for {}", sql, name, e);
			throw new DatabaseException("Cannot execute queryEmployeeByName", e);
		}
		return emps;
	}
	
	
	/******* Utility Methods *******/
	private List<Employee> getAndHandleResults(PreparedStatement stmt) throws SQLException {
		List<Employee> emps = new ArrayList<>();
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			int empNumber = rs.getInt("empno");
			String empName = rs.getString("ename");
			String job = rs.getString("job");
			int mgrNumber = rs.getInt("mgr");
			String hireDate = rs.getString("hiredate");
			BigDecimal sal = rs.getBigDecimal("sal");
			BigDecimal comm = rs.getBigDecimal("comm");
			int deptNumber = rs.getInt("deptno");

			Employee emp = new Employee(empNumber, job, empName, mgrNumber, hireDate, comm, sal, deptNumber);

			emps.add(emp);
		}
		return emps;
	}



}
