package com.fidelity.integration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;

import com.fidelity.model.Employee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class EmployeeDaoOracleImpl extends EmployeeDao {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public EmployeeDaoOracleImpl(DataSource datasource) {
		super(datasource);
		// TODO Auto-generated constructor stub
	}

	

	public List<Employee> queryAllEmployees() {
		String sql = "SELECT empno, ename, job, mgr, hiredate, sal, comm, deptno FROM emp";
		List<Employee> employeeList = new ArrayList<>();
		PreparedStatement statement = null;
		Connection connection = null;

		try {
			connection = getDataSource().getConnection();
			statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Employee employee = new Employee(resultSet.getInt("EMPNO"),
						resultSet.getString("ENAME"),
						resultSet.getString("JOB"),
						resultSet.getInt("MGR"),
						resultSet.getDate("hiredate").toLocalDate(),
						resultSet.getBigDecimal("SAL"),
						resultSet.getBigDecimal("COMM"),
						resultSet.getInt("DEPTNO"));
				employeeList.add(employee);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Cannot execute SQL query for emp: {}", e);
			throw new DatabaseException("Cannot execute SQL query for emp: ", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("Cannot execute close connection", e);
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					logger.error("Cannot execute close statement", e);
				}
			}
		}
		return employeeList;

	}

	@Override
	public Employee queryEmployeeById(int empNo) {
		// TODO Auto-generated method stub
		String sql = "SELECT empno,ename,job,mgr,hiredate,sal,comm,deptno from emp where empno=?";
		Employee employee = null;
		PreparedStatement statement = null;
		Connection connection = null;

		try {
			connection = getDataSource().getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, empNo);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				employee = new Employee(resultSet.getInt("EMPNO"),
						resultSet.getString("ENAME"),
						resultSet.getString("JOB"),
						resultSet.getInt("MGR"),
						resultSet.getDate("hiredate").toLocalDate(),
						resultSet.getBigDecimal("SAL"),
						resultSet.getBigDecimal("COMM"),
						resultSet.getInt("DEPTNO"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Cannot execute SQL query for emp: {}", e);
			throw new DatabaseException("Cannot execute SQL query for emp: ", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("Cannot execute close connection", e);
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					logger.error("Cannot execute close statement", e);
				}
			}
		}
		return employee;
	}



	@Override
	public void insertEmployee(Employee emp) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO emp(empno,ename,job,mgr,hiredate,sal,comm,deptno) VAlUES (?,?,?,?,?,?,?,?)";
		
		PreparedStatement statement = null;
		Connection connection = null;

		try {
			connection = getDataSource().getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, emp.getEmpNumber());
			statement.setString(2, emp.getEmpName());
			statement.setString(3, emp.getJob());
			statement.setInt(4, emp.getMgrNumber());

			Date hireDateSql = Date.valueOf(emp.getHireDate());
			statement.setDate(5, hireDateSql);

			statement.setBigDecimal(6, emp.getSalary());
			statement.setBigDecimal(7, emp.getComm());
			statement.setInt(8, emp.getDeptNumber());

			statement.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
			logger.error("Cannot execute SQL query for emp: {}", e);
			throw new DatabaseException("Cannot execute SQL query for emp: ", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("Cannot execute close connection", e);
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					logger.error("Cannot execute close statement", e);
				}
			}
		}
		
	}



	@Override
	public Employee updateEmployee(Employee emp) {
		// TODO Auto-generated method stub
		String sql = "UPDATE emp SET ename = ?, job = ? WHERE empno=?";
		
		PreparedStatement statement = null;
		Connection connection = null;

		try {
			connection = getDataSource().getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, emp.getEmpName());
			statement.setString(2, emp.getJob());
			statement.setInt(3, emp.getEmpNumber());
			

			statement.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
			logger.error("Cannot execute SQL query for emp: {}", e);
			throw new DatabaseException("Cannot execute SQL query for emp: ", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("Cannot execute close connection", e);
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					logger.error("Cannot execute close statement", e);
				}
			}
		}
		return emp;
	}



	@Override
	public void deleteEmployee(int empNo) {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM emp where empno=?";

		PreparedStatement statement = null;
		Connection connection = null;

		try {
			connection = getDataSource().getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, empNo);
			


			statement.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
			logger.error("Cannot execute SQL query for emp: {}", e);
			throw new DatabaseException("Cannot execute SQL query for emp: ", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("Cannot execute close connection", e);
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					logger.error("Cannot execute close statement", e);
				}
			}
		}
	}
	
	
	public List<Employee> queryAllEmployeesWithPerformanceReview() {
		String sql = "SELECT e.empno, e.ename, e.job, e.mgr, e.hiredate, e.sal, e.comm, e.deptno,pf.perf_rev_code,pf.perf_rev_name FROM emp e JOIN emp_perf pf ON e.empno=pf.empno";
		List<Employee> employeeList = new ArrayList<>();
		PreparedStatement statement = null;
		Connection connection = null;

		try {
			connection = getDataSource().getConnection();
			statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				Employee employee = new Employee(resultSet.getInt("EMPNO"),
						resultSet.getString("ENAME"),
						resultSet.getString("JOB"),
						resultSet.getInt("MGR"),
						resultSet.getDate("hiredate").toLocalDate(),
						resultSet.getBigDecimal("SAL"),
						resultSet.getBigDecimal("COMM"),
						resultSet.getInt("DEPTNO")
						);
				int perfRev=resultSet.getInt("PERF_REV_CODE");
				PerformanceReviewResult perfRes=PerformanceReviewResult.of(perfRev);
				employee.setReview(perfRes);
				employeeList.add(employee);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Cannot execute SQL query for emp: {}", e);
			throw new DatabaseException("Cannot execute SQL query for emp: ", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("Cannot execute close connection", e);
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					logger.error("Cannot execute close statement", e);
				}
			}
		}
		return employeeList;

	}
	
	public Employee queryAllEmployeesWithPerformanceReviewByEmpno(int empNo) {
		String sql = "SELECT e.empno, e.ename, e.job, e.mgr, e.hiredate, e.sal, e.comm, e.deptno,pf.perf_rev_code,pf.perf_rev_name FROM emp e JOIN emp_perf pf ON e.empno=pf.empno WHERE e.empno=?";
		PreparedStatement statement = null;
		Connection connection = null;
		Employee employee=null;

		try {
			connection = getDataSource().getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, empNo);
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				employee = new Employee(resultSet.getInt("EMPNO"),
						resultSet.getString("ENAME"),
						resultSet.getString("JOB"),
						resultSet.getInt("MGR"),
						resultSet.getDate("hiredate").toLocalDate(),
						resultSet.getBigDecimal("SAL"),
						resultSet.getBigDecimal("COMM"),
						resultSet.getInt("DEPTNO")
						);
				int perfRev=resultSet.getInt("PERF_REV_CODE");
				PerformanceReviewResult perfRes=PerformanceReviewResult.of(perfRev);
				employee.setReview(perfRes);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Cannot execute SQL query for emp: {}", e);
			throw new DatabaseException("Cannot execute SQL query for emp: ", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("Cannot execute close connection", e);
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					logger.error("Cannot execute close statement", e);
				}
			}
		}
		return employee;

	}
	
	
	
}
