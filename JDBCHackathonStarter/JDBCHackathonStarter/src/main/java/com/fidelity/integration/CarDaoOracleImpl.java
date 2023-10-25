package com.fidelity.integration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fidelity.model.Car;
import com.fidelity.model.CarBodyType;
import com.fidelity.model.CarEngine;
import com.fidelity.model.CarEngineType;

public class CarDaoOracleImpl implements CarDao {

	private DataSource dataSource;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public CarDaoOracleImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

 

    private void handleDatabaseException(String message, SQLException e) {
        e.printStackTrace();
        logger.error(message, e);
        throw new DatabaseException(message, e);
    }

 

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("Cannot close connection", e);
            }
        }
    }

    private void closeStatement(PreparedStatement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.error("Cannot close statement", e);
            }
        }
    }

 

	@Override
	public List<Car> getCars() {
		String sql = "SELECT e.id,e.make,e.model,b.body_type_name,c.engine_type,c.engine_size_l,c.horsepower,e.manufacture_date from e_car e left join e_car_engine c on e.id=c.id JOIN e_car_body_type b ON e.body_type=b.body_type ORDER BY e.id";
		return executeQuery(sql);

	}

 

	private List<Car> executeQuery(String sql, Object... params ) {
		List<Car> carList = new ArrayList<>();
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(sql);
			
			for (int i = 0; i < params.length; i++) {
				statement.setObject(i + 1, params[i]);
			}
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String make = resultSet.getString("make");
				String model = resultSet.getString("model");
				String bodyTypeName = resultSet.getString("body_type_name");
				int engineType = resultSet.getInt("engine_type");
				CarEngine carEngine=null;
				if (!resultSet.wasNull())
				{
					CarEngineType carEngineType = CarEngineType.of(engineType);
					float horsepower = resultSet.getFloat("horsepower");
					float engineSize = resultSet.getFloat("engine_size_l");
					if(resultSet.wasNull()) {
						engineSize = 0;
					}
					carEngine = new CarEngine(id, carEngineType, horsepower, engineSize);
				}
				
				LocalDate manufactureDate = resultSet.getDate("manufacture_date").toLocalDate();

				Car car = new Car(id, make, model, bodyTypeName, carEngine, manufactureDate);

				carList.add(car);
			}
		} catch (SQLException e) {
			handleDatabaseException("Cannot execute SQL query", e);
		} finally {
			closeConnection(connection);
			closeStatement(statement);
		}
		return carList;
	}

 

	@Override
	public void insertCar(Car car){
		
		
		Connection conn=null;
		try{
			conn = dataSource.getConnection();
			insertCarOnly(conn, car);

			if (car.getEngine() != null) {  // do not insert a null engine into database
				insertCarEngine(conn, car);
			}
		} 
		catch (SQLException e) {
			String msg = "Cannot execute insertCar for " + car;
			logger.error(msg + ": " + e);
			throw new DatabaseException(msg, e);
		}finally {
			closeConnection(conn);;
		}
		
		
	}

	private void insertCarOnly(Connection conn, Car car) throws SQLException{
		Objects.requireNonNull(car);
		String carSql = "INSERT INTO e_car(id, make, model, body_type, manufacture_date) VALUES (?,?,?,?,?)";
		CarBodyType bodyType = CarBodyType.of(car.getBodyTypeName());
		int bodyTypeNumber = bodyType.getCode();
		PreparedStatement statement=null;
		try{
			statement=conn.prepareStatement(carSql);
			statement.setInt(1, car.getId());
			statement.setString(2, car.getMake());
			statement.setString(3, car.getModel());
			statement.setInt(4, bodyTypeNumber);
			statement.setDate(5, Date.valueOf(car.getManufactureDate()));
			int rowsUpdated = statement.executeUpdate();

			if (rowsUpdated == 0) {
				throw new DatabaseException("there is no car with id " + car.getId());
			}
		} 
		catch (SQLException e) {
			//logger.error("Cannot execute update (" + carSql + ") for " + car, e);
			throw new DatabaseException("Cannot insert/update car", e);
		}finally {
			closeStatement(statement);
		}
		
	}
	
	private void insertCarEngine(Connection conn, Car car) throws SQLException {
		Objects.requireNonNull(car.getEngine());
		String engineSql = "INSERT INTO e_car_engine(id, engine_type, engine_size_l, horsepower) VALUES (?,?,?,?)";
		PreparedStatement stmt = conn.prepareStatement(engineSql);
		try {
			CarEngine engine = car.getEngine();
			stmt.setInt(1, engine.getCarId());
			stmt.setInt(2, engine.getEngineType().getCode());
			if (engine.getEngineSizeInLiters() > 0.0f) {
				stmt.setFloat(3, engine.getEngineSizeInLiters());
			}
			else {
				stmt.setNull(3, Types.FLOAT);
			}
			stmt.setFloat(4, engine.getHorsepower());
			stmt.executeUpdate();
		} catch (SQLException e) {
			logger.error("Cannot execute update (" + engineSql + ") for " + car, e);
			throw new DatabaseException("Cannot insert/update car", e);
		}finally {
			closeStatement(stmt);
		}
	}
	
	@Override
	public void updateCar(Car car) {
		// TODO Auto-generated method stub
		Objects.requireNonNull(car);
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			updateCar(conn, car);
			
			if (car.getEngine() != null) {  // do not insert a null engine into database
				updateCarEngine(conn, car);
			}
		} 
		catch (SQLException e) {
			String msg = "Cannot execute updateCar for " + car;
			logger.error(msg + ": " + e);
			throw new DatabaseException(msg, e);
		}
		finally {
			closeConnection(conn);
		}
		

	}
	
	private void updateCar(Connection conn, Car car) throws SQLException {
		Objects.requireNonNull(car);
		String carSql = """
				UPDATE scott.e_car
				SET
					make = ?,
					model = ?,
					body_type = ?,
					manufacture_date = ?
				WHERE
					e_car.id = ?
			""";
		PreparedStatement stmt=null;
		try  {
			stmt = conn.prepareStatement(carSql);
			stmt.setString(1, car.getMake());
			stmt.setString(2, car.getModel());

			CarBodyType bodyType = CarBodyType.of(car.getBodyTypeName() /*String*/);
			stmt.setInt(3, bodyType.getCode() /* int 1,2,3,4 */);

			LocalDate manufactureDate = car.getManufactureDate();
			java.sql.Date mDate = Date.valueOf(manufactureDate);
			stmt.setDate(4, mDate);

			stmt.setInt(5, car.getId());

			int rowsUpdated = stmt.executeUpdate();

			if (rowsUpdated == 0) {
				throw new DatabaseException("there is no car with id " + car.getId());
			}
		} 
		catch (SQLException e) {
			logger.error("Cannot execute update (" + carSql + ") for " + car, e);
			throw new DatabaseException("Cannot insert/update car", e);
		}finally {
			closeStatement(stmt);
		}
	}
	
	private void updateCarEngine(Connection conn, Car car) throws SQLException {
		Objects.requireNonNull(car.getEngine());
		String engineSql = """
				UPDATE scott.e_car_engine 
				SET 
					engine_type = ?,
					engine_size_l = ?,
					horsepower = ?
				WHERE 
					id = ?
			""";
		PreparedStatement stmt=null;
		
		try  {
			stmt = conn.prepareStatement(engineSql);
			CarEngine engine = car.getEngine();
			stmt.setInt(1, engine.getEngineType().getCode());
			if (engine.getEngineSizeInLiters() > 0.0f) {
				stmt.setFloat(2, engine.getEngineSizeInLiters());
			}
			else {
				stmt.setNull(2, Types.FLOAT);
			}
			stmt.setFloat(3, engine.getHorsepower());
			stmt.setInt(4, engine.getCarId());
			stmt.executeUpdate();
		}catch (SQLException e) {
			logger.error("Cannot execute update (" + engineSql + ") for " + car, e);
			throw new DatabaseException("Cannot insert/update car", e);
		}finally {
			closeStatement(stmt);
		} 
	}

	@Override
	public void deleteCar(int carId) {
		// TODO Auto-generated method stub
		String engineSql = "DELETE FROM e_car_engine where id=?";
		String carSql = "DELETE FROM e_car where id=?";
		if(!carEngineExists(carId))
		{
			executeUpdate(carSql, carId);
		}
		else
		{
			executeUpdate(engineSql, carId);
			executeUpdate(carSql, carId);
		}
		

 

	}
	
	private boolean carEngineExists(int carId) {
	    String query = "SELECT COUNT(*) FROM e_car_engine WHERE id = ?";
	    try (Connection connection = dataSource.getConnection();
	         PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setInt(1, carId);
	        try (ResultSet resultSet = statement.executeQuery()) {
	            if (resultSet.next()) {
	                int rowCount = resultSet.getInt(1);
	                return rowCount > 0; 
	            }
	        }
	    } catch (SQLException e) {
	        handleDatabaseException("Error checking if car exists", e);
	    }
	    return false; 
	}
	private void executeUpdate(String sql, Object... params) {
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            int rowsUpdated=statement.executeUpdate();
            if (rowsUpdated == 0) {
				throw new DatabaseException("there is no car with this id ");
			}
        } catch (SQLException e) {
            handleDatabaseException("Cannot execute SQL query", e);
        } finally {
        	closeConnection(connection);
			closeStatement(statement);;
        }
    }
}
